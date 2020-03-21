; INCLUDES
Include "includes/const.bb"
Include "includes/types.bb"
; Include "includes/json-parser.bb"

Include "includes/settings.bb"
LoadDefaultSettings()

; INITIALIZE KEYBOARD SETTINGS
Global keyForward, keyBackward, keyLeft, keyRight, keyJump, keyEnterVehicle, keyHandbrake, keyHorn, keyLight, keyRadio, keyAttack, keyNextWeapon, keyPreviousWeapon, keyFastSave, keyChat, keyFart, keyMob, keyPowerUp
; ParseJSON("sys/keys.json")

;WaitKey
;End

AppTitle "Bounty Hunt City - v." + version
SeedRnd MilliSecs()

Graphics3D screen\width, screen\height, screen\depth, screen\mode
SetBuffer BackBuffer()
Global frameTimer = CreateTimer(screen\frameRate)

Include "includes/sound.bb"
Include "includes/gui.bb"
Include "includes/hud.bb"
Include "includes/maps.bb"
Include "includes/gameplay.bb"

Global ms ; contains the current milliseconds

Global cam = CreateCamera()
PositionEntity cam, 0, 10, 0
RotateEntity cam, 90, 0, 0

Global light = CreateLight()
RotateEntity light, 90, 0, 0

; LOAD TEST MAP
LoadMap("test-city")

; LOAD EXAMPLE VEHICLE
Global carTexture = LoadTexture("gfx/vehicles/car1.png", 4)

; TODO: remove again
Global test = LoadImage("includes/cash.png")

InitPlayer()

CreateCar()

While Not KeyHit(KEY_ESCAPE)
	ms = MilliSecs()

	Cls
	WaitTimer frameTimer

	AnimateWater()
	PlayerControls()
	CollisionDetection()
	
	UpdateWorld
	RenderWorld

	Hud()

	; DEBUG TEXTS
	text 0, 0, "Active Weapon: " + player\activeWeapon

	col = CountCollisions(player\entity)
	Text 0, 0, "Kollision(en):"
	For i = 1 To col
		Text 000, i * 20, "Entity=" + CollisionEntity(player\entity, i)
		Text 150, i * 20, "Surface=" + CollisionSurface(player\entity, i)
		Text 300, i * 20, "Triangle=" + CollisionTriangle(player\entity, i)
	Next
	
	Flip screen\vsync
Wend
End



Function CreateCar()
	car = CreateMesh() 
	surf = CreateSurface(car) 
	
	v0 = AddVertex (surf, -1, 1, -1, 0, 1) 
	v1 = AddVertex (surf,  1, 1, -1, 1, 1) 
	v2 = AddVertex (surf,  1, 1,  1, 1, 0)
	v3 = AddVertex (surf, -1, 1,  1, 0, 0)
	
	tri1 = AddTriangle (surf,v0,v2,v1)
	tri2 = AddTriangle (surf,v0,v3,v2)
	
	EntityTexture car, carTexture

	EntityBox car, -1, -1, -2.5, 2, 2, 5
	EntityType car, COLLISION_VEHICLE

	PositionEntity car, 10, 0, 0
	ScaleEntity car, 1.5, 3, 3
End Function
