Global hudCash = LoadImage("gfx/hud/cash.png")
Global hudLife = LoadImage("gfx/hud/life.png")
Global hudCops = LoadImage("gfx/hud/cops.png")
Global hudReticle = LoadImage("gfx/hud/reticle.png")
Global hudClock = LoadImage("gfx/hud/clock.png")

Const hudPaddingHorizontal = 20
Const hudPaddingVertical = 10

Global testHudFont = LoadFont("Verdana", 24, True)

Function Hud()
    DrawImage hudClock, screen\width - ImageWidth(hudClock) - hudPaddingHorizontal, hudPaddingVertical
    DrawImage hudCash, screen\width - ImageWidth(hudCash) - hudPaddingHorizontal, hudPaddingVertical + ImageHeight(hudClock) + 10
    DrawImage hudLife, screen\width - ImageWidth(hudLife) - hudPaddingHorizontal, hudPaddingVertical + ImageHeight(hudClock) + ImageHeight(hudCash) + 20

    SetFont testHudFont
    Local timeText$ = GetTime()
    Text screen\width - ImageWidth(hudClock) - hudPaddingHorizontal - StringWidth(timeText) - 20, hudPaddingVertical + 10, timeText

    Local cashText$ = CashText()
    Text screen\width - ImageWidth(hudCash) - hudPaddingHorizontal - StringWidth(cashText) - 20, hudPaddingVertical + ImageHeight(hudClock) + 23, cashText

    Local lifeText$ = Str(Int(player\energy))
    Text screen\width - ImageWidth(hudLife) - hudPaddingHorizontal - StringWidth(lifeText) - 20, hudPaddingVertical + ImageHeight(hudClock) + ImageHeight(hudCash) + 30, lifeText

    DrawImage hudCops, hudPaddingHorizontal, hudPaddingVertical
    Text hudPaddingHorizontal + ImageWidth(hudCops) + 20, hudPaddingVertical + 12, player\copLevel

    DrawImage hudReticle, hudPaddingHorizontal, hudPaddingVertical + ImageHeight(hudCops) + 15
End Function

Function CashText$()
    Local i
    Local cashString$ = Str(player\cash)
    Local cashLength = Len(cashString)
    Local formattedCashString$ = ""
    For i = 0 To cashLength-1
        formattedCashString = Mid(cashString, cashLength-i, 1) + formattedCashString
        If (i+1) Mod 3 = 0 And i > 0 And i <> cashLength-1 Then formattedCashString = "." + formattedCashString
    Next

    Return formattedCashString
End Function