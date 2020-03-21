Type T_Vehicle_Definition
	Field name$
	Field entity
	Field maxSpeed#
	Field energy#
End Type

Type T_Car
    Field entity
    Field speed#
    Field maxSpeed#
    Field energy#
    Field maxEnergy#
    Field motorChannel
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
    car\maxSpeed = 90
    car\energy = 100
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

	EntityBox vehicle, -1*width, -1, -1*height, width*2, 2, height*2
	EntityType vehicle, COLLISION_VEHICLE

	ScaleEntity vehicle, width, 2, height
    HideEntity vehicle

    Return vehicle
End Function

Function UpdateVehicles()
    Local car.T_Car
    For car.T_Car = Each T_Car
        If car\energy <= 0 Then
            ; TODO: add explosion effect
            ; TODO: use separate texture or only darken color?
            EntityColor car\entity, 50, 50, 50
        EndIf
    Next
End Function