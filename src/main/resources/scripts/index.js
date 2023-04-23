const { ipcRenderer  } = require ("electron");
let listOfEventLists = [];


function weekButtonClick(){
    let week = document.querySelector("#weekViewContainer");
    let month = document.querySelector("#monthViewContainer");
    let year = document.querySelector("#yearViewContainer");

    week.style.display = "grid";
    month.style.display = "none";
    year.style.display = "none";
}

function monthButtonClick(){
    let week = document.querySelector("#weekViewContainer");
    let month = document.querySelector("#monthViewContainer");
    let year = document.querySelector("#yearViewContainer");

    week.style.display = "none";
    month.style.display = "grid";
    year.style.display = "none";

    loadMonthView()
}

function yearButtonClick(){
    let week = document.querySelector("#weekViewContainer");
    let month = document.querySelector("#monthViewContainer");
    let year = document.querySelector("#yearViewContainer");

    week.style.display = "none";
    month.style.display = "none";
    year.style.display = "grid";
}

function updateDate(){
    const date = new Date();
    const options = { year: 'numeric', month: 'long', day: 'numeric' };
    const dateString = date.toLocaleDateString('en-US', options);
    document.querySelector("#current_day").innerHTML = dateString.replace(/\d+(st|nd|rd|th)/, '$&, ');
}

function getDatesForMonth(year, month) {
    const daysInMonth = new Date(year, month + 1, 0).getDate();
    const firstDay = new Date(year, month, 1).getDay();
    const dates = [];

    // Add dates from previous month if necessary
    for (let i = 0; i < firstDay; i++) {
        const prevMonthDate = new Date(year, month, -i).getDate();
        dates.unshift({ day: prevMonthDate, month: month-1 });
    }

    // Add dates from current month
    for (let i = 1; i <= daysInMonth; i++) {
        dates.push({ day: i.toString().padStart(2, '0'), month: month });
    }

    // Add dates from next month if necessary
    const lastDay = new Date(year, month + 1, 0).getDay();
    for (let i = 1; i < 7 - lastDay; i++) {
        dates.push({ day: i.toString().padStart(2, '0'), month: month + 1 });
    }

    return dates;
}

function loadMonthView(){
    const today = new Date();
    const year = today.getFullYear();
    const month = today.getMonth();
    const dates = getDatesForMonth(year, month);
    listOfEventLists = [];

    const boxes = document.querySelector('#monthViewContainer').querySelectorAll('.box');

    for (let i = 0; i < boxes.length; i++) {
        boxes[i].innerHTML = `<label class="monthNumber">1</label>`;

        const label = boxes[i].querySelector('.monthNumber');
        const date = dates[i];

        if (date) {
            label.innerText = `${date.day}`;
            fetch('http://127.0.0.1:50000/getDayEvents?' + new URLSearchParams({
                date: `${year}-${(date.month + 1).toString().padStart(2, '0')}-${date.day} 00:00`
            }))
                .then(response => response.json())
                .then(data => {
                    if (data.length > 0) {
                        listOfEventLists.push({
                            date: new Date(year, date.month, date.day, 0, 0, 0, 0),
                            events: data});
                        for (let event of data){
                            boxes[i].innerHTML +=
                                `<div id="${event.id}" class="event" onclick="showEvent(this.id);">
                                    <label for="${event.id}:checkbox">${event.name}</label>
                                    <input type="checkbox" id="${event.id}:checkbox">
                                </div>`;
                        }

                    }
                })
                .catch(error => console.error(error));

        } else {
            label.innerText = '';
        }
    }

    console.log(listOfEventLists);
}

function showEvent(id){
    // console.log(`Event id: ${id}`);

    let finalEvent = listOfEventLists.find(eventList => eventList.events.find(event => event.id === id)).events.find(event => event.id === id);

    document.querySelector("#selectedItemTitle").innerText = finalEvent.name;
    document.querySelector("#selectedItemDescription").innerText = finalEvent.description;

    const startDateArray = finalEvent.dateAttributes.startDateTime;
    const startDate = new Date(startDateArray[0], startDateArray[1] - 1, startDateArray[2], startDateArray[3], startDateArray[4])
        .toLocaleDateString('en-US') + ' ' + new Date(startDateArray[0], startDateArray[1] - 1, startDateArray[2], startDateArray[3], startDateArray[4])
        .toLocaleTimeString('en-US', { hour: 'numeric', minute: 'numeric' });

    const endDateArray = finalEvent.dateAttributes.endDateTime;
    const endDate = new Date(endDateArray[0], endDateArray[1] - 1, endDateArray[2], endDateArray[3], endDateArray[4])
        .toLocaleDateString('en-US') + ' ' + new Date(endDateArray[0], endDateArray[1] - 1, endDateArray[2], endDateArray[3], endDateArray[4])
        .toLocaleTimeString('en-US', { hour: 'numeric', minute: 'numeric' });


    document.querySelector("#selectedItemStartDate").innerText = startDate;
    document.querySelector("#selectedItemEndDate").innerText = endDate;

    let attributeGrid = document.querySelector("#selectedItemAttributeGrid")
    attributeGrid.innerHTML = "";

    for(let attribute of finalEvent.stringAttributes){
        attributeGrid.innerHTML += `<input class="selectedItemAttributeField" value="${attribute.name}" onblur="onEventUpdated('${id}', this.value, '${attribute.id}', 'name', 'String');">
                                    <input class="selectedItemAttributeText" value="${attribute.value}" onblur="onEventUpdated('${id}', this.value, '${attribute.id}', 'value', 'String');">`
    }

    for(let attribute of finalEvent.intAttributes){
        attributeGrid.innerHTML += `<input class="selectedItemAttributeField" value="${attribute.name}" onblur="onEventUpdated('${id}', this.value, '${attribute.id}', 'name', 'int')">
                                    <input class="selectedItemAttributeText" value="${attribute.value}" onblur="onEventUpdated('${id}', this.value, '${attribute.id}', 'value', 'int')">`
    }



}

function onEventUpdated(id, value, attributeID, nameOrValue, type){
    let finalEvent = listOfEventLists.find(eventList => eventList.events.find(event => event.id === id)).events.find(event => event.id === id);

    if(type === "String"){
        if(nameOrValue === "name"){
            finalEvent.stringAttributes.find(attribute => attribute.id === attributeID).name = value;
        } else {
            finalEvent.stringAttributes.find(attribute => attribute.id === attributeID).value = value;
        }
    }
    else{
        if(nameOrValue === "name"){
            finalEvent.intAttributes.find(attribute => attribute.id === attributeID).name = value;
        } else {
            finalEvent.intAttributes.find(attribute => attribute.id === attributeID).value = value;
        }
    }

    console.log(finalEvent.stringAttributes);

    //merged list of string and int attributes
    let mergedAttributes = finalEvent.stringAttributes.concat(finalEvent.intAttributes);

    fetch('http://127.0.0.1:50000/updateEvent?' + new URLSearchParams({
        id: id,
        name: finalEvent.name,
        description: finalEvent.description,
        dateAttributeName: finalEvent.dateAttributes.name,
        dateAttributeDescription: finalEvent.dateAttributes.description,
        startDateTime: new Date(Date.UTC(...finalEvent.dateAttributes.startDateTime)).toISOString().replace('T', ' ').replace(/\.\d{3}Z/, ''),
        endDateTime: new Date(Date.UTC(...finalEvent.dateAttributes.endDateTime)).toISOString().replace('T', ' ').replace(/\.\d{3}Z/, '')
    }), {
        method: 'POST',
        body:mergedAttributes
    })
        .then(response => response.json())
        .then(data => {
            console.log(data);
        })
        .catch(error => console.error(error));



}


updateDate();
setInterval(updateDate, 1000);
document.querySelector("#weekViewContainer").style.display = "none";
document.querySelector("#monthViewContainer").style.display = "grid";
document.querySelector("#yearViewContainer").style.display = "none";

loadMonthView();

// function view2(){
//     ipcRenderer.send("view2");
// }