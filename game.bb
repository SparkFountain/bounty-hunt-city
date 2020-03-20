; INCLUDES
Include "includes/const.bb"
Include "includes/json-parser.bb"

; INITIALIZE KEYBOARD SETTINGS
Global keyForward, keyBackward, keyLeft, keyRight, keyJump, keyEnterVehicle, keyHandbrake, keyHorn, keyLight, keyRadio, keyAttack, keyNextWeapon, keyPreviousWeapon, keyFastSave, keyChat, keyFart, keyMob, keyPowerUp
ParseJSON("sys/keys.json")


WaitKey
End

AppTitle "Bounty Hunt City"

Graphics3D 800, 600, 32, 2
SetBuffer BackBuffer()
Global frameTimer = CreateTimer(60)

Global cam = CreateCamera()
PositionEntity cam, 0, 10, 0
RotateEntity cam, 90, 0, 0

Global light = CreateLight()
RotateEntity light, 90, 0, 0

; LOAD EXAMPLE VEHICLE
Global carTexture = LoadTexture("gfx/vehicles/car1.png", 4)

CreateCar()

;Local i
;For i=-20 To 20 Step 5
;	Local cube = CreateCube()
;	PositionEntity cube, i, 0, 0
;	EntityTexture cube, carTexture
;Next


Global plane = CreatePlane()
EntityColor plane, 0, 255, 0


While Not KeyHit(1)
	Cls
	WaitTimer frameTimer
	
	UpdateWorld
	RenderWorld
	
	MoveCube()
	
	Flip
Wend
End




; FUNCTIONS
Function MoveCube()
	If KeyDown(ARROW_UP) Then MoveEntity cam, 0, 0.1, 0 
	If KeyDown(ARROW_DOWN) Then MoveEntity cam, 0, -0.1, 0 
End Function

Function CreateCar()
	mesh = CreateMesh() 
	surf = CreateSurface(mesh) 
	
	v0 = AddVertex (surf, -1, 1, -1, 0, 0) 
	v1 = AddVertex (surf,  1, 1, -1, 0, 1) 
	v2 = AddVertex (surf,  1, 1,  1, 1, 1)
	v3 = AddVertex (surf, -1, 1,  1, 1, 0)
	
	tri1 = AddTriangle (surf,v0,v2,v1)
	tri2 = AddTriangle (surf,v0,v3,v2)
	
	EntityTexture mesh, carTexture
	
	ScaleEntity mesh, 1, 1, 0.5
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D