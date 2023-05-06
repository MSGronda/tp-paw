let params = new URLSearchParams(window.location.search);
let dir = params.get("dir");

const orderBtns = [
    ['difficulty-order','easy','diffuclty-down','diffuclty-up'],
    ['timedemand-order','timedemanding','timedemand-down','timedemand-up'],
]
const asc='asc'
const desc = 'desc'
for(let elem in orderBtns) {
    const btnElem = document.getElementById(orderBtns[elem][0])
    if(typeof btnElem !== null && elem !== 'undefined' ){
        btnElem.addEventListener('click',
            function() {
                url = window.location.href;
                url = addOrUpdateParam(url,"order",orderBtns[elem][1]);
                if(dir !== null && dir === asc){
                    url = addOrUpdateParam(url,"dir",desc);
                } else {
                    url = addOrUpdateParam(url,"dir",asc);
                }
                window.location.href = url;
            });
        if(orderBtns[elem][0] === 'difficulty-order' && dir === desc){
            const section1 = document.getElementById(orderBtns[elem][2])
            const section2 = document.getElementById(orderBtns[elem][3])
            section1.style.display = "block";
            section2.style.display = "none";
        } else if(orderBtns[elem][0] === 'timedemand-order' && dir === desc) {
            const section1 = document.getElementById(orderBtns[elem][2])
            const section2 = document.getElementById(orderBtns[elem][3])
            section1.style.display = "block";
            section2.style.display = "none";
        }else if(orderBtns[elem][0] === 'difficulty-order' && dir === asc){
            const section1 = document.getElementById(orderBtns[elem][2])
            const section2 = document.getElementById(orderBtns[elem][3])
            section1.style.display = "none";
            section2.style.display = "block";
        } else {
            const section1 = document.getElementById(orderBtns[elem][2])
            const section2 = document.getElementById(orderBtns[elem][3])
            section1.style.display = "none";
            section2.style.display = "block";
        }
    }
}