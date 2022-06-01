let navigation = document.getElementsByClassName("photo-menu-container");
let services = document.getElementsByClassName("service");
let backButton = document.getElementsByClassName("service return");
let extraMenu = document.getElementsByClassName("services-container");
let tiles = document.getElementsByClassName('accessible');
let markedTile = document.getElementsByClassName('tile-marked-container');
let boardSizeButton = document.getElementsByClassName('board-size-selector')


for (let i = 0; i < services.length; i++) {
    services[i].addEventListener('mouseout', event => unhoverExtraMenu(services[i]));
    services[i].addEventListener('mouseover', event => hoverExtraMenu(services[i]));
}

if (document.getElementById("index") !== null) {
    for (let i = 0; i < navigation.length; i++) {
        navigation[i].addEventListener('mouseout', event => hide(navigation[i]));
        navigation[i].addEventListener('mouseover', event => reveal(navigation[i]));
    }
    document.getElementById("nav1").addEventListener('click', event => revealExtraMenu(extraMenu[2]));
    document.getElementById("nav3").addEventListener('click', event => revealExtraMenu(extraMenu[0]));
    document.getElementById("nav4").addEventListener('click', event => revealExtraMenu(extraMenu[1]));
    backButton[0].addEventListener('click', event => hideExtraMenu(extraMenu[0]));
    backButton[1].addEventListener('click', event => hideExtraMenu(extraMenu[1]));
    backButton[2].addEventListener('click', event => hideExtraMenu(extraMenu[2]));


}

if (document.getElementById("rules") !== null) {
    document.getElementById("rulesRight").addEventListener("click", event => nextRule());
    document.getElementById("rulesLeft").addEventListener("click", event => prevRule());
}

if (document.getElementById("scores") !== null) {
    for (let i = 0; i < boardSizeButton.length; i++) {
        boardSizeButton[i].addEventListener('mouseover', event => markChosenBoardSize(boardSizeButton[i]))
        boardSizeButton[i].addEventListener('mouseout', event => unmarkChosenBoardSize(boardSizeButton[i]))
    }
}


if (document.getElementById("taptiles") !== null) {
    for (let i = 0; i < tiles.length; i++) {
        tiles[i].addEventListener('mouseout', event => noHoveredImage());
        tiles[i].addEventListener('mouseover', event => hoveredImage(tiles[i]));
    }
    if (markedTile.length === 1) {
        let markedClue = document.getElementById('markedTile');
        markedClue.style.background = "white";
    }
}


function revealExtraMenu(menu) {
    for (let i = 0; i < navigation.length; i++) {
        navigation[i].style.opacity = "0";
    }
    setTimeout(function () {
        for (let i = 0; i < navigation.length; i++) {
            navigation[i].style.display = "none";
        }
    }, 350)
    menu.style.display = "unset";
    setTimeout(function () {
        menu.style.transform = "translateY(-50%) translateX(-50%) scale(1)";
    }, 1)
}


function hideExtraMenu(menu) {
    let pause
    if (menu.id === "services-menu") {
        menu.style.transform = "translateY(-50%) translateX(-20%) scale(0.6)";
        pause = 350
    }
    if (menu.id === "login-menu") {
        menu.style.transform = "translateY(-50%) translateX(53%) scale(0.6)";
        pause = 650
    }
    if (menu.id === "board-size-menu") {
        menu.style.transform = "translateY(-50%) translateX(-165%) scale(0.6)";
        pause = 650
    }
    menu.style.backgroundColor = "rgb(70, 171, 130)";
    menu.style.boxShadow = "0 0 2vw 0.5vw black";
    setTimeout(function () {
        menu.style.display = "none";
    }, pause);
    for (let i = 0; i < navigation.length; i++) {
        navigation[i].style.display = "flex";
    }
    menu.style.boxShadow = "0 0 0.1vw 0.3vw black";
    menu.style.backgroundColor = "rgb(68, 75, 103)";
    setTimeout(function () {
            for (let i = 0; i < navigation.length; i++) {
                navigation[i].style.opacity = "1";
            }
        },
        (pause - 100));
}

function unhoverExtraMenu(services) {
    services.style.opacity = '0.7';
    services.children[0].style.width = '0%';
}

function hoverExtraMenu(services) {
    services.style.opacity = '1';
    services.children[0].style.width = '100%';
}

function hoveredImage(tile) {
    let hovered = document.getElementById('hoveredTile');
    let newSource = tile.src.slice(0, -3);
    hovered.parentElement.style.background = "white";
    hovered.src = newSource + "svg";
}

function noHoveredImage() {
    let hovered = document.getElementById('hoveredTile');
    hovered.parentElement.style.background = "rgba(255,255,255,0.5)";

    hovered.src = "/images/taptiles/blank.png";
}

function hide(navigation) {
    if(navigation.id === "nav1"){
        navigation.children[1].style.opacity = "0"
    }
    navigation.children[0].className = 'menu-text';
    navigation.children[0].children[0].style.width = '0';
}

function reveal(navigation) {
    if(navigation.id === "nav1"){
        navigation.children[1].style.opacity = "1"
    }
    navigation.children[0].className = 'menu-text visible-text';
    navigation.children[0].children[0].style.width = '110%';
}

let ruleNumber = 1;

function nextRule() {
    ruleNumber++;
    if (ruleNumber > 5) {
        ruleNumber = 5;
    } else {
        arrowUpdate();
        printRule(ruleNumber);
    }
}

function prevRule() {
    ruleNumber--;
    if (ruleNumber < 1) {
        ruleNumber = 1;
    } else {
        arrowUpdate();
        printRule(ruleNumber);
    }
}

function arrowUpdate() {
    if (ruleNumber === 5) {
        document.getElementById("rulesRight").style.opacity = "0";
        document.getElementById("rulesRight").style.cursor = "unset";
    } else {
        document.getElementById("rulesRight").style.opacity = "1";
        document.getElementById("rulesRight").style.cursor = "pointer";
    }
    if (ruleNumber === 1) {
        document.getElementById("rulesLeft").style.opacity = "0";
        document.getElementById("rulesLeft").style.cursor = "unset";
    } else {
        document.getElementById("rulesLeft").style.opacity = "1";
        document.getElementById("rulesLeft").style.cursor = "pointer";
    }
}

function printRule(number) {
    if (number === 1) {
        document.getElementById("ruleDesciption").innerHTML = "IF YOU HOVER ACCESSIBLE TILE, IT WILL DISPLAY IN THE RIGHT BORDER. " +
            " ACCESSIBLE TILE ARE THE ONES WHICH ARE FREE FROM THE FRONT OR BACK AND LEFT OR RIGHT. BOTH CONDITIONS MUST BE TRUE.";
        document.getElementById("ruleTitle").innerHTML = "ACCESSIBLE TILES";
        document.getElementById("ruleVideo").src = "/images/taptiles/rule1.gif";
    }
    if (number === 2) {
        document.getElementById("ruleDesciption").innerHTML = "YOU CAN MARK ONLY ACCESSIBLE TILES. IF YOU MARK ACCESSIBLE TILE, IT WILL APPERA IN LEFT BORDER."
        document.getElementById("ruleTitle").innerHTML = "MARK TILE";
        document.getElementById("ruleVideo").src = "/images/taptiles/rule2.gif";
    }
    if (number === 3) {
        document.getElementById("ruleDesciption").innerHTML = "IF YOU MATCH TILES WITH THE SAME SYMBOL, TILES WILL DISAPPEAR. EVERY MATCHED TILES" +
            " GIVE YOU SCORE. YOU ALSO GET COMBO POINTS FOR FAST MATCHING."
        document.getElementById("ruleTitle").innerHTML = "MATCH TILES";
        document.getElementById("ruleVideo").src = "/images/taptiles/rule3.gif";
    }
    if (number === 4) {
        document.getElementById("ruleDesciption").innerHTML = "YOU CAN ROTATE BOARD BY CLICKING ON LEFT OR RIGHT ARROW AT THE BOTTOM OF THE SCREEN." +
            +" YOUR MARKED TILE REMAINS MARKED AFTER ROTATION."
        document.getElementById("ruleTitle").innerHTML = "ROTATE BOARD";
        document.getElementById("ruleVideo").src = "/images/taptiles/rule4.gif";
    }
    if (number === 5) {
        document.getElementById("ruleDesciption").innerHTML = "IF YOU ARE HELPLESS. USE ROTATION BUTTON AT THE TOP OF THE SCREEN. ALL NOT SOLVED TILES CHANGES ITS POSITION." +
            " BE CAREFUL YOU MIGHT BE EVEN MORE HELPLESS AFTER SHUFFLE."
        document.getElementById("ruleTitle").innerHTML = "SHUFFLE BOARD";
        document.getElementById("ruleVideo").src = "/images/taptiles/rule5.gif";
    }
}

function markChosenBoardSize(button) {
    if (button.className === 'board-size-selector') {
        button.className = 'board-size-selector hover'
    }
}

function unmarkChosenBoardSize(button) {
    if (button.className !== 'board-size-selector chosen') {
        button.className = 'board-size-selector'
    }
}