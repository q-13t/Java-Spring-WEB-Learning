
function confirm(params) {
    let address = document.getElementById("address");
    let country = document.getElementById("country");
    if (address.value !== "" && country.value !== "") {
        document.getElementById("purchase-form").submit();
    } else {
        if (address.value === "") {
            document.getElementById("address_error").classList.remove("hidden");
        } else {
            document.getElementById("address_error").classList.add("hidden");
        }
        if (country.value === "") {
            document.getElementById("country_error").classList.remove("hidden");
        } else {
            document.getElementById("country_error").classList.add("hidden");
        }
    }
}