Global hudCash = LoadImage("gfx/hud/cash.png")
Global hudLife = LoadAnimImage("gfx/hud/life.png", 32, 32, 0, 20)

Const hudPaddingHorizontal = 20
Const hudPaddingVertical = 10

Function Hud()
    DrawImage hudCash, screen\width - ImageWidth(hudCash) - hudPaddingHorizontal, hudPaddingVertical
    DrawImage hudLife, screen\width - ImageWidth(hudCash) - hudPaddingHorizontal - ImageWidth(hudLife)*2, hudPaddingVertical + 10, (player\energy / player\maxEnergy) * 20 - 1
End Function