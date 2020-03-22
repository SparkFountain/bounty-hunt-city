Global hudCash = LoadImage("gfx/hud/cash.png")
Global hudLife = LoadImage("gfx/hud/life.png")
Global hudCops = LoadImage("gfx/hud/cops.png")
Global hudReticle = LoadImage("gfx/hud/reticle.png")

Const hudPaddingHorizontal = 20
Const hudPaddingVertical = 10

Global testHudFont = LoadFont("Verdana", 24, True)

Function Hud()
    DrawImage hudCash, screen\width - ImageWidth(hudCash) - hudPaddingHorizontal, hudPaddingVertical
    DrawImage hudLife, screen\width - ImageWidth(hudLife) - hudPaddingHorizontal, hudPaddingVertical + ImageHeight(hudCash) + 10

    SetFont testHudFont
    Local cashText$ = CashText()
    Text screen\width - ImageWidth(hudCash) - hudPaddingHorizontal - StringWidth(cashText) - 20, hudPaddingVertical + 15, cashText

    Local lifeText$ = Str(Int(player\energy))
    Text screen\width - ImageWidth(hudLife) - hudPaddingHorizontal - StringWidth(lifeText) - 20, hudPaddingVertical + ImageHeight(hudCash) + 15, lifeText

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