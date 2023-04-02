
function handleFile(input) {
    const id = input.id.replace("-in", "");
    const { files: [file] } = input;
    const previewImage = document.querySelector(`#${id}`);

    if (!previewImage) {
        console.error(`Preview image with ID #${id} not found.`);
        return;
    }

    const reader = new FileReader();
    reader.onload = () => {
        previewImage.src = reader.result;
    }
    reader.onerror = (error) => {
        console.error(error);
    }

    reader.readAsDataURL(file);
}
