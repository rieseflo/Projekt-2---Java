function checkFiles(files) {
    console.log(files);

    if (files.length != 1) {
        alert("Bitte genau eine Datei hochladen.")
        return;
    }

    const fileSize = files[0].size / 1024 / 1024; // in MiB
    if (fileSize > 10) {
        alert("Datei zu gross (max. 10Mb)");
        return;
    }

    answerPart.style.visibility = "visible";
    const file = files[0];

    // Preview
    if (file) {
        preview.src = URL.createObjectURL(files[0])
    }

    // Upload
    const formData = new FormData();
    for (const name in files) {
        formData.append("image", files[name]);
    }

    fetch('/analyze', {
        method: 'POST',
        headers: {
        },
        body: formData
    }).then(
        response => {
            console.log(response)
            response.text().then(function (text) {
                // Parse the JSON string into a JavaScript object
                const result = JSON.parse(text);

                // Clear the previous content of the answer div
                answer.innerHTML = "";

                // Create a new table element
                const table = document.createElement('table');
                table.classList.add('table', 'table-bordered', 'table-striped'); // Adding Bootstrap classes for styling

                // Create table header row
                const headerRow = table.insertRow();
                const classNameHeader = headerRow.insertCell();
                classNameHeader.textContent = 'Class Name';
                classNameHeader.classList.add('font-weight-bold'); // Add custom class for bold font
                const probabilityHeader = headerRow.insertCell();
                probabilityHeader.textContent = 'Probability';
                probabilityHeader.classList.add('font-weight-bold'); // Add custom class for bold font

                // Iterate over the result array and populate the table rows
                result.forEach(item => {
                    const row = table.insertRow();
                    const classNameCell = row.insertCell();
                    classNameCell.textContent = item.className;
                    const probabilityCell = row.insertCell();

                    // Format probability as percentage
                    const probabilityPercent = (item.probability * 100).toFixed(1) + '%';
                    probabilityCell.textContent = probabilityPercent;
                });

                // Append the table to the answer div
                answer.appendChild(table);
            });
        }
        ).then(
            success => console.log(success)
        ).catch(
            error => console.log(error)
        );
    }