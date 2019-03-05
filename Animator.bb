; This tool allows you to take animation screenshots of an object.
; © 2014 by Spark Fountain

AppTitle("Animation Tool for Bounty Hunt City")
Graphics3D(400,400,32,2)
SetBuffer(BackBuffer())
Global frameTimer = CreateTimer(60)

Global cam = CreateCamera()
RotateEntity(cam,90,0,0)
Const defaultZoom# = 13
PositionEntity(cam,0,defaultZoom,0)
Global light = CreateLight()
PositionEntity(light,0,10,0)
RotateEntity(light,90,0,0)


;select object
Const path$ = "gfx/humans/human3.b3d"
Global animObject = LoadAnimMesh(path)

;animation frame
Global animFrame# = 0


While(Not(KeyHit(1)))
	Cls()
	WaitTimer(frameTimer)
	
	MoveCamera()
	SetFrame()
	
	UpdateWorld()
	RenderWorld()
	
	;draw frame of 66x66 around the image
	Color(0,255,0)
	Rect(200-33,200-33,66,66,0)
	
	Text(0,0,"Frame: "+animFrame)
	Text(0,20,"Total frames: "+AnimLength(animObject))
	Text(0,40,"Zoom: "+EntityY(cam)+" (default: "+defaultZoom+")")
	
	Flip(1)
Wend
End


Function MoveCamera()
	If(KeyDown(201)) Then MoveEntity(cam,0,0,0.1)
	If(KeyDown(209)) Then MoveEntity(cam,0,0,-0.1)
	
	If(KeyDown(17)) Then PositionEntity(cam,EntityX(cam),EntityY(cam),EntityZ(cam)+0.1)
	If(KeyDown(31)) Then PositionEntity(cam,EntityX(cam),EntityY(cam),EntityZ(cam)-0.1)
	If(KeyDown(30)) Then PositionEntity(cam,EntityX(cam)-0.1,EntityY(cam),EntityZ(cam))
	If(KeyDown(32)) Then PositionEntity(cam,EntityX(cam)+0.1,EntityY(cam),EntityZ(cam))
End Function


Function SetFrame()
	If(KeyHit(200) And (animFrame<30)) Then animFrame=animFrame+0.2 : SetAnimTime(animObject,animFrame)
	If(KeyHit(208) And (animFrame>0)) Then animFrame=animFrame-0.2 : SetAnimTime(animObject,animFrame)
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D