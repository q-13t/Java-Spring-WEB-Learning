let items_selected = new Map();

document.addEventListener('DOMContentLoaded', function () {
    document.querySelectorAll('.selection span').forEach((span, index) => {
        const id = span.id.replace("span", "");
        console.log(id);
        const element = document.getElementById(`${id}quantity`);
        if (element.value !== "1")
            addThisItem(document.getElementById(id));
    });
});


function addThisItem(item) {
    const id = item.id;

    if (!items_selected.has(id)) {
        const amount = document.getElementById(id + "quantity").value;
        items_selected.set(id, amount);
        item.parentElement.classList.add("item-selected");
        item.innerHTML = "Remove"
    } else {
        items_selected.delete(id);
        item.parentElement.classList.remove("item-selected");
        item.innerHTML = "Add"
    }

    let item_cont = document.querySelector("#item_count");
    item_cont.innerHTML = items_selected.size;
    item_cont.classList.remove("play-add_animation");
    void item_cont.offsetWidth;
    if (items_selected.size > 0) {
        item_cont.classList.add("play-add_animation");
    }
}

function purchase(params) {
    const entryValues = [];

    for (const [key, value] of items_selected.entries()) {
        entryValues.push(`${key}:${value}`);
    }

    document.querySelector('#order-data').value = entryValues.join(',');
    document.querySelector('#purchase-form').submit();
}


function updateQuantity(button) {
    let input = document.getElementById(button.id.replace(/(add|sub)/, "quantity"));
    switch (button.value) {
        case "+": {
            if (parseInt(input.value) < 1000) {
                input.value = parseInt(input.value) + 1;
            }

            break;
        }
        case "-": {
            if (parseInt(input.value) > 0) {
                input.value = parseInt(input.value) - 1;
            }
            break;
        }
    }
}
function validateNumber(element) {
    element.value = Math.max(0, Math.min(1000, element.value));
}
