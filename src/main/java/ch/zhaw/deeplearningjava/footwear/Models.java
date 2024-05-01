/*
 * Copyright 2020 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance
 * with the License. A copy of the License is located at
 *
 * http://aws.amazon.com/apache2.0/
 *
 * or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
package ch.zhaw.deeplearningjava.footwear;

import ai.djl.Model;
import ai.djl.ndarray.types.Shape;
import ai.djl.nn.Blocks;
import ai.djl.nn.SequentialBlock;
import ai.djl.nn.convolutional.Conv2d;
import ai.djl.nn.core.Linear;
import ai.djl.nn.norm.Dropout;
import ai.djl.nn.pooling.Pool;
import ai.djl.nn.Activation;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/** A helper class loads and saves model. */
public final class Models {

    // the number of classification labels: 88 different gemstone types
    public static final int NUM_OF_OUTPUT = 88;

    // the height and width for pre-processing of the image
    public static final int IMAGE_HEIGHT = 100;
    public static final int IMAGE_WIDTH = 100;

    // the name of the model
    public static final String MODEL_NAME = "gemstoneclassifier";

    private Models() {}

    public static Model getModel() {
        // Create a new instance of an empty model
        Model model = Model.newInstance(MODEL_NAME);
    
        SequentialBlock block = new SequentialBlock();
        block.add(Conv2d.builder().setFilters(32).setKernelShape(new Shape(3, 3)).optStride(new Shape(1, 1)).optPadding(new Shape(1, 1)).build());
        block.add(Activation.reluBlock());
        block.add(Pool.maxPool2dBlock(new Shape(2, 2), new Shape(2, 2)));

        block.add(Conv2d.builder().setFilters(64).setKernelShape(new Shape(3, 3)).optStride(new Shape(1, 1)).optPadding(new Shape(1, 1)).build());
        block.add(Activation.reluBlock());
        block.add(Pool.maxPool2dBlock(new Shape(2, 2), new Shape(2, 2)));

        block.add(Conv2d.builder().setFilters(128).setKernelShape(new Shape(3, 3)).optStride(new Shape(1, 1)).optPadding(new Shape(1, 1)).build());
        block.add(Activation.reluBlock());
        block.add(Pool.maxPool2dBlock(new Shape(2, 2), new Shape(2, 2)));

        block.add(Conv2d.builder().setFilters(256).setKernelShape(new Shape(3, 3)).optStride(new Shape(1, 1)).optPadding(new Shape(1, 1)).build());
        block.add(Activation.reluBlock());
        block.add(Pool.maxPool2dBlock(new Shape(2, 2), new Shape(2, 2)));

        block.add(Conv2d.builder().setFilters(256).setKernelShape(new Shape(3, 3)).optStride(new Shape(1, 1)).optPadding(new Shape(1, 1)).build());
        block.add(Activation.reluBlock());
        block.add(Pool.maxPool2dBlock(new Shape(2, 2), new Shape(2, 2)));

        // Reshape the output to a 1D tensor
        block.add(Blocks.batchFlattenBlock()); // This will flatten the output

        block.add(Dropout.builder().optRate(0.5f).build());

        block.add(Linear.builder().setUnits(512).build());
        block.add(Activation.reluBlock());

        block.add(Linear.builder().setUnits(NUM_OF_OUTPUT).build());

        model.setBlock(block);
        return model;
    }

    public static void saveSynset(Path modelDir, List<String> synset) throws IOException {
        Path synsetFile = modelDir.resolve("synset.txt");
        try (Writer writer = Files.newBufferedWriter(synsetFile)) {
            writer.write(String.join("\n", synset));
        }
    }
}
