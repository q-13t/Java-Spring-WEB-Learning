let items_selected = [];

function addThisItem(item) {

    if (!items_selected.includes(item)) {
        items_selected.push(item);
        item.parentElement.classList.add("item-selected");
        item.innerHTML = "Remove"
    } else {
        for (var i = items_selected.length - 1; i >= 0; i--) {
            if (items_selected[i] === item) {
                items_selected.splice(i, 1);
                item.parentElement.classList.remove("item-selected");
                item.innerHTML = "Add"
            }
        }
    }
    let item_cont = document.querySelector("#item_count");
    item_cont.innerHTML = items_selected.length;
    item_cont.classList.remove("play-add_animation");
    void item_cont.offsetWidth;
    if (items_selected.length > 0) {
        item_cont.classList.add("play-add_animation");
    }
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
    if (element.value > 1000) {
        element.value = 1000;
    } else if (element.value < 0) {
        element.value = 0;

    }
}