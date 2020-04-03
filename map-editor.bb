; INCLUDES
Include "includes/const.bb"
Include "includes/types.bb"

Include "includes/settings.bb"
LoadDefaultSettings()

AppTitle "Bounty Hunt City - v." + version
SeedRnd MilliSecs()

Graphics3D screen\width, screen\height, screen\depth, screen\mode
SetBuffer BackBuffer()
Global frameTimer = CreateTimer(screen\frameRate)
AntiAlias True

While Not KeyHit(KEY_ESCAPE)
    UpdateWorld
    RenderWorld
Wend
End