const { ipcRenderer  } = require ("electron");

function view2(){
    ipcRenderer.send("view2");
}