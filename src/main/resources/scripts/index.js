const { ipcRenderer  } = require ("electron");

function view2(){
    ipcRenderer.send("view2");
}

function changeToWeekView(){
    document.getElementsByClassName("monthViewContainer").style.display = 'none';
    document.getElementsByClassName("yearViewContainer").style.display = 'none';
    document.getElementsByClassName("weekViewContainer").style.display = 'block';
}

function changeToMonthView(){
    document.getElementsByClassName("weekViewContainer").style.display = 'none';
    document.getElementsByClassName("yearViewContainer").style.display = 'none';
    document.getElementsByClassName("monthViewContainer").style.display = 'block';
}

function changeToYearView(){
    document.getElementsByClassName("weekViewContainer").style.display = 'none';
    document.getElementsByClassName("monthViewContainer").style.display = 'none';
    document.getElementsByClassName("yearViewContainer").style.display = 'block';
}

function addEventView(){
    document.getElementsByClassName("selectedItemContainer").style.display = 'none';
    document.getElementsByClassName("addItemContainer").style.display = 'block';
}

function submitNewEvent(){
    
}