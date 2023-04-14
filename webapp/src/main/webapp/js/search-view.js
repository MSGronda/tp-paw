// Se corre cada vez que se abre la pagina

let params = new URLSearchParams(window.location.search);

const filterSection = document.getElementById('filter-section');

if(params.keys().next().done) // no tiene parametros
    filterSection.style.display = "none"
else
    filterSection.style.display = "flex"

const sectionMinCredits = document.getElementById('remove-min-credits-param-section');
const sectionMedCredits = document.getElementById('remove-med-credits-param-section');
const sectionMaxCredits = document.getElementById('remove-max-credits-param-section');


const sectionDpt = document.getElementById('remove-sistemas-param-section')

let credits = params.get("credits")

sectionMinCredits.style.display = "none";
sectionMedCredits.style.display = "none";
sectionMaxCredits.style.display = "none";

switch (credits){
    case null: break;
    case "3":
        sectionMinCredits.style.display = "block";
        break;
    case "6": sectionMedCredits.style.display = "block"; break;
    case "9": sectionMaxCredits.style.display = "block"; break;
}

let dpt = params.get("department")

sectionDpt.style.display = "none";

switch (dpt){
    case null: break;
    case "Sistemas Digitales y Datos": sectionDpt.style.display = "block";
}


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

// Filter section
const toggleBtn = document.getElementById('toggle-filters');

toggleBtn.addEventListener('click', function() {
    if(filterSection.style.display === 'none')
        filterSection.style.display = 'flex';
    else
        filterSection.style.display = 'none';
});

const orderByNameBtn = document.getElementById('order-by-name');
const orderByCreditsBtn = document.getElementById('order-by-credits');
const orderByIdBtn = document.getElementById('order-by-id');


// Order by section
//TODO unificar todas las function() en una

orderByNameBtn.addEventListener('click', function() {
    window.location.href = addOrUpdateParam(window.location.href,"ob","subname");
});

orderByCreditsBtn.addEventListener('click', function() {
    window.location.href = addOrUpdateParam(window.location.href,"ob","credits");
});


orderByIdBtn.addEventListener('click', function() {
    window.location.href = addOrUpdateParam(window.location.href,"ob","id");
});


// Credit filters

const minCreditFilterBtn = document.getElementById('min-credit-filter');
const medCreditFilterBtn = document.getElementById('med-credit-filter');
const maxCreditFilterBtn = document.getElementById('max-credit-filter');

minCreditFilterBtn.addEventListener('click', function() {
    window.location.href = addOrUpdateParam(window.location.href,"credits","3");
});

medCreditFilterBtn.addEventListener('click', function() {
    window.location.href = addOrUpdateParam(window.location.href,"credits","6");
});

maxCreditFilterBtn.addEventListener('click', function() {
    window.location.href = addOrUpdateParam(window.location.href,"credits","9");
});


const removeMinCreditFilterBtn = document.getElementById('remove-min-credits-filter');
const removeMedCreditFilterBtn = document.getElementById('remove-med-credits-filter');
const removeMaxCreditFilterBtn = document.getElementById('remove-max-credits-filter');


removeMinCreditFilterBtn.addEventListener('click', function() {
    window.location.href = removeURLParam(window.location.href,"credits");
});

removeMedCreditFilterBtn.addEventListener('click', function() {
    window.location.href = removeURLParam(window.location.href,"credits");
});

removeMaxCreditFilterBtn.addEventListener('click', function() {
    window.location.href = removeURLParam(window.location.href,"credits");
});


// Department filters

const sistemasFilterBtn = document.getElementById('sistemas-credit-filter');
sistemasFilterBtn.addEventListener('click', function() {
    window.location.href = addOrUpdateParam(window.location.href,"department","Sistemas Digitales y Datos");
});

const removeSistemasFilter = document.getElementById('remove-sistemas-param-section');
removeSistemasFilter.addEventListener('click', function() {
    window.location.href = removeURLParam(window.location.href,"department");
});




