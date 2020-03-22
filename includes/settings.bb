Global settingsFile
Global screen.T_Screen
Global version$

Type T_Screen
    Field width, height, depth, mode
    Field frameRate
    Field vsync
End Type

Function LoadSettings()
    ; TODO: implement JSON reading
End Function

Function LoadDefaultSettings()
    screen.T_Screen = new T_Screen
    screen\width = 1920
    screen\height = 1080
    screen\depth = 32
    screen\mode = SCREEN_MODE_FULLSCREEN
    screen\frameRate = 60
    screen\vsync = 1

    version = "0.0.1"
End Function