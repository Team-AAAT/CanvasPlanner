const { app, BrowserWindow, ipcMain, ipcRenderer, dialog} = require("electron")
const electron = require("electron")
const { spawn } = require('child_process');
const path = require('path');
const electronReload = require('electron-reload');

let mainWindow
let javaProcess
electronReload(__dirname);

function createWindow() {
    const display = electron.screen.getPrimaryDisplay()
    const { width, height } = display.bounds
    mainWindow.setBounds({
        x: -8,
        y: 0,
        width: width,
        height: height})
    mainWindow.setAlwaysOnTop(false)
    mainWindow.loadFile('views/index.html')
    mainWindow.setMenuBarVisibility(false)
    mainWindow.setAutoHideMenuBar(true)
}

function startJavaBackend() {
    const jarPath = path.join(__dirname, 'CanvasPlanner.jar');
    javaProcess = spawn('java', ['-jar', jarPath]);

    javaProcess.stdout.on('data', (data) => {
        console.log(`stdout: ${data}`);
    });

    javaProcess.stderr.on('data', (data) => {
        console.error(`stderr: ${data}`);
    });

    javaProcess.on('close', (code) => {
        console.log(`child process exited with code ${code}`);
    });
}

function stopJavaBackend() {
    if (javaProcess) {
        javaProcess.kill('SIGINT');
        javaProcess = null;
    }
}


app.whenReady().then(() => {
    startJavaBackend();
    mainWindow = new BrowserWindow( {
        webPreferences: {
            nodeIntegration: true,
            contextIsolation: false
        }
    })
    createWindow() // creates the new window

    mainWindow.on('closed', () => {
        stopJavaBackend();
        app.quit();
    });
})
ipcMain.on("view2", (event, arg) => {
    mainWindow.loadFile('views/view2.html')
})
ipcMain.on("home", (event, arg) => {
    mainWindow.loadFile('views/index.html')
})
