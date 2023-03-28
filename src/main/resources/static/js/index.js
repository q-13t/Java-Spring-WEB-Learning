let items_selected = [];

function addThisItem(item) {
    // console.log(item);


    // items_selected.splice(item);

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


    // console.log(items_selected);
    document.querySelector("#item_count").innerHTML = items_selected.length;
}