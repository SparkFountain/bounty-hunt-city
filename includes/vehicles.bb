Type T_Vehicle_Definition
	Field name$
	Field entity
	Field maxSpeed#
    Field acceleration#
    Field friction#
	Field energy#
    Field engineSound
    Field hornSound
End Type

Type T_Car
    Field entity
    Field speed#
    Field maxSpeed#
    Field acceleration#
    Field friction#
    Field energy#
    Field maxEnergy#
    Field engineSound
    Field engineChannel
    Field hornSound
	Field hornChannel
	Field damageEmitter, damageEmitterLevel
End Type

Type T_Boat

End Type

Function LoadVehicles()
    ; TODO: implement
End Function

Function LoadTestVehicles()
    Local car.T_Vehicle_Definition = new T_Vehicle_Definition
    car\name = "Brummi"
    car\entity = CreateVehicleEntity(1.3, 2.6, LoadTexture("gfx/vehicles/car1.png", TEXTURE_MASKED))
    car\maxSpeed = 0.5
    car\acceleration = 0.007
    car\friction = 0.003
    car\energy = 100
    car\engineSound = Load3DSound("sfx/vehicles/car1.ogg")
    car\hornSound = Load3DSound("sfx/vehicles/horn1.ogg")
End Function

Function CreateVehicleEntity(width#, height#, texture)
    ; create mesh with a single surface
    vehicle = CreateMesh()
	surf = CreateSurface(vehicle)
	
    ; create vertex points for triangles
	v0 = AddVertex (surf, -1, 1, -1, 0, 1)
	v1 = AddVertex (surf,  1, 1, -1, 1, 1)
	v2 = AddVertex (surf,  1, 1,  1, 1, 0)
	v3 = AddVertex (surf, -1, 1,  1, 0, 0)
	
    ; create triangles
	tri1 = AddTriangle (surf,v0,v2,v1)
	tri2 = AddTriangle (surf,v0,v3,v2)
	
    ; apply vehicle texture
	EntityTexture vehicle, texture

	EntityBox vehicle, -1*width, -5, -1*height, width*2, 10, height*2
	EntityType vehicle, COLLISION_VEHICLE

	ScaleEntity vehicle, width, 2, height
    HideEntity vehicle

    Return vehicle
End Function

Function UpdateVehicles()
    Local car.T_Car
    For car.T_Car = Each T_Car
        If car\energy <= 0 And car\energy > -1000 Then
            ; TODO: add explosion effect
            ; TODO: use separate texture
            EntityColor car\entity, 50, 50, 50
            StopChannel car\engineChannel
            EmitSound(soundExplosion(Rand(0,0)), player\listener)
            car\energy = -1000
        Else
            If car\speed <> 0 Then ; slow down because of friction
                If car\speed > 0 Then
                    car\speed = car\speed - car\friction
                    If car\speed < 0 Then car\speed = 0 ; do not move backwards
                Else
                    car\speed = car\speed + car\friction
                    If car\speed > 0 Then car\speed = 0 ; do not move forwards
                EndIf
            EndIf
            If car\speed > car\maxSpeed Then car\speed = car\maxSpeed ; speed limit

            ; DebugLog "Acceleration: " + car\acceleration
            ; DebugLog "Friction: " + car\friction
            ; DebugLog "Speed: " + car\speed

            ; If ChannelPlaying(car\hornChannel) Then DebugLog "Horn playing"

            ; position car on terrain
            Local x# = EntityX(car\entity)
            Local y# = EntityY(car\entity)
            Local z# = EntityZ(car\entity)
            Local deltaY# = TerrainY(map, x, 0, z) - y
            TranslateEntity car\entity, 0, deltaY, 0

            ; move car
            MoveEntity car\entity, 0, 0, car\speed

            ; calculate engine pitch depending on speed
            ChannelPitch car\engineChannel, 44100 + car\speed * 100000

            ; emit engine sound
            If Not ChannelPlaying(car\engineChannel) Then
                ; emit sound
                car\engineChannel = EmitSound(car\engineSound, car\entity)
            EndIf
        EndIf
    Next
End Function