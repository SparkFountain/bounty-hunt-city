; INCLUDES
Include "includes/const.bb"
Include "includes/types.bb"
; Include "includes/json-parser.bb"

Include "includes/settings.bb"
LoadDefaultSettings()

; INITIALIZE KEYBOARD SETTINGS
Global keyForward, keyBackward, keyLeft, keyRight, keyJump, keyEnterVehicle, keyHandbrake, keyHorn, keyLight, keyRadio, keyAttack, keyNextWeapon, keyPreviousWeapon, keyFastSave, keyChat, keyFart, keyMob, keyPowerUp
; ParseJSON("sys/keys.json")

AppTitle "Bounty Hunt City - v." + version
SeedRnd MilliSecs()

Graphics3D screen\width, screen\height, screen\depth, screen\mode
SetBuffer BackBuffer()
Global frameTimer = CreateTimer(screen\frameRate)

Include "includes/sound.bb"
Include "includes/gui.bb"
Include "includes/hud.bb"
Include "includes/maps.bb"
Include "includes/weapons.bb"
Include "includes/vehicles.bb"
Include "includes/gameplay.bb"
Include "includes/environment.bb"

Global ms ; contains the current milliseconds

Global cam = CreateCamera()
PositionEntity cam, 0, 10, 0
RotateEntity cam, 90, 0, 0

Global light = CreateLight()
RotateEntity light, 90, 0, 0

LoadTestWeapons()
LoadTestVehicles()

; LOAD TEST MAP
LoadMap("test-city")
InitTestMap()

InitPlayer()

While Not KeyHit(KEY_ESCAPE)
	ms = MilliSecs()

	Cls
	WaitTimer frameTimer

	DayNightCicle()
	AnimateWater()
	PlayerControls()
	UpdateAmmo()
	UpdateVehicles()
	PlayRadio()
	
	UpdateWorld
	RenderWorld

	Hud()

	; DEBUG INFORMATION
	Text screen\width / 2, 0, "Active Weapon: " + player\activeWeapon, screen\width / 2
	
	Flip screen\vsync
Wend
End
