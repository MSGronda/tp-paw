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


// Se corre cada vez que se abre la pagina

let params = new URLSearchParams(window.location.search);

const filterSection = document.getElementById('filter-section');

if(params.keys().next().done) // no tiene parametros
    filterSection.style.display = "none"
else
    filterSection.style.display = "flex"


// Department filters
let dpt = params.get("department")

const dptFilterBtns = [
    ['sistemas-credit-filter', 'Sistemas Digitales y Datos', 'remove-sistemas-filter', 'remove-sistemas-param-section'],
]

for(let elem in dptFilterBtns){

    // Comportamiento de boton de aplicar filtro
    document.getElementById(dptFilterBtns[elem][0]).addEventListener('click',
        function() { window.location.href = addOrUpdateParam(window.location.href,"department",dptFilterBtns[elem][1])});

    // Comportamiento de boton de eliminar filtro
    document.getElementById(dptFilterBtns[elem][2]).addEventListener('click',
        function() { window.location.href = removeURLParam(window.location.href,"department")});

    // Visibiliad de boton de eliminar filtro
    const section = document.getElementById(dptFilterBtns[elem][3])
    console.log(dptFilterBtns[elem][3])
    if(dpt === dptFilterBtns[elem][1])
        section.style.display = "block";
    else
        section.style.display = "none"
}



// Credit filters
let credits = params.get("credits")

const creditFilterBtns = [
    ['min-credit-filter', "3", 'remove-min-credits-filter', 'remove-min-credits-param-section'],
    ['med-credit-filter', "6", 'remove-med-credits-filter', 'remove-med-credits-param-section'],
    ['max-credit-filter', "9", 'remove-max-credits-filter', 'remove-max-credits-param-section']
]
for(let elem in creditFilterBtns)
{
    document.getElementById(creditFilterBtns[elem][0]).addEventListener('click',
        function() { window.location.href = addOrUpdateParam(window.location.href,"credits",creditFilterBtns[elem][1]);});
    document.getElementById(creditFilterBtns[elem][2]).addEventListener('click',
        function() { window.location.href = removeURLParam(window.location.href,"credits")});
    const section = document.getElementById(creditFilterBtns[elem][3])
    if(credits === creditFilterBtns[elem][1])
        section.style.display = "block";
    else
        section.style.display = "none"
}



// Filter section
const toggleBtn = document.getElementById('toggle-filters');

toggleBtn.addEventListener('click', function() {
    if(filterSection.style.display === 'none')
        filterSection.style.display = 'flex';
    else
        filterSection.style.display = 'none';
});


// Order by btns
const addOrderByBtns = [['order-by-name', "subname"], ['order-by-credits', "credits"], ['order-by-id', "id"]]
for(let elem in addOrderByBtns)
{
    document.getElementById(addOrderByBtns[elem][0]).addEventListener('click',
        function() { window.location.href = addOrUpdateParam(window.location.href,"ob",addOrderByBtns[elem][1]);});
}





