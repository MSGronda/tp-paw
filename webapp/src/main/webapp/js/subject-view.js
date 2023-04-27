
// ****IMPORTAR FUNCIONES DE SEARCH-VIEW PARA NO DUPLICAR CODIGO****
function addOrUpdateParam(url, param, value) {
    var regex = new RegExp("([?&])" + param + "=.*?(&|$)", "i");
    var separator = url.indexOf('?') !== -1 ? "&" : "?";
    if (url.match(regex)) {
        return url.replace(regex, '$1' + param + "=" + value + '$2');
    }
    else
        return url + separator + param + "=" + value;
}

function removeURLParam(url, param) {
    var urlsections = url.split('?');

    if (urlsections.length >= 2) {
        var prefix = encodeURIComponent(param) + '=';
        var params = urlsections[1].split(/[&;]/g);

        for (var i = params.length - 1; i >= 0; i--) {
            if (decodeURIComponent(params[i]).lastIndexOf(prefix, 0) !== -1) {
                params.splice(i, 1);
            }
        }
        url = urlsections[0] + (params.length > 0 ? '?' + params.join('&') : '');
        return url;
    }
    else
        return url;

}

let params = new URLSearchParams(window.location.search);
let order = params.get("order");

const orderBtns = [
    ['difficulty-order','easy'],
    ['timedemand-order','timedemanding'],
    ['oldest-order','oldest']
]
for(let elem in orderBtns) {
    document.getElementById(orderBtns[elem][0]).addEventListener('click',
        function() { window.location.href = addOrUpdateParam(window.location.href,"order",orderBtns[elem][1]);})
}