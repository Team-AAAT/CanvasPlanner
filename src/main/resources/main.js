const { app, BrowserWindow, ipcMain, ipcRenderer, dialog} = require("electron")
const electron = require("electron")
var mainWindow
const electronReload = require('electron-reload');
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
app.whenReady().then(() => {
    mainWindow = new BrowserWindow( {
        webPreferences: {
            nodeIntegration: true,
            contextIsolation: false
        }
    })
    createWindow() // creates the new window
})
ipcMain.on("view2", (event, arg) => {
    mainWindow.loadFile('views/view2.html')
})
ipcMain.on("home", (event, arg) => {
    mainWindow.loadFile('views/index.html')
})
