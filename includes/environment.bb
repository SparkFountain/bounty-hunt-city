Global gameTime = 60*10 ; time in minutes - begin at 10 am
Global nextTick = 0

Function GetTime$()
    Local time$

    ; hours
    local hours = gameTime / 60
    If hours < 10 Then
        time = "0" + hours
    Else
        time = Str(hours)
    EndIf

    ; delimiter
    time = time + ":"

    ; minutes
    Local minutes = gameTime Mod 60
    If minutes < 10 Then
        time = time + "0" + minutes
    Else
        time = time + Str(minutes)
    EndIf

    Return time
End Function

Function DayNightCicle()
    If nextTick < ms Then
        nextTick = ms + 1000
        gameTime = gameTime + 1

        If gameTime > 60*5.5 And gameTime <= 60*7 Then
            Local brightness# = 100
            AmbientLight brightness, brightness, brightness
        EndIf
    EndIf
End Function