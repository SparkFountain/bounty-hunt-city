Global soundPistol = LoadSound("sfx/weapons/pistol.ogg")

Global pistolBullet = CreateSphere(4)
EntityColor pistolBullet, 200, 40, 20
ScaleEntity pistolBullet, 0.12, 0.12, 0.12
HideEntity pistolBullet

Dim lastWeaponTrigger(9) ; time stamp when each weapon was triggered the last time


Type T_Melee
    Field name$
End Type

Type T_Pistol
	Field name$
	Field shotInterval
	Field ammoPerMagazine
	Field damage#
	Field bulletSpeed#
	Field bulletLifeTime
    Field bulletEntity
    Field shotSound
End Type

Type T_MachinePistol
    Field name$
End Type

Type T_MachineGun
    Field name$
End Type

Type T_RocketLauncher
    Field name$
End Type

Type T_Grenade
    Field name$
End Type

Type T_MolotovCocktail
    Field name$
End Type

Type T_FlameThrower
    Field name$
End Type

Type T_Electro
    Field name$
End Type

Type T_Rifle
    Field name$
End Type

Type T_Bullet
    Field entity
    Field speed#
    Field lifeTime
    Field damage#
End Type

Function LoadWeapons()
    ; TODO implement
End Function

Function LoadTestWeapons()
    Local pistol.T_Pistol = New T_Pistol
    pistol\name = "Pistol"
    pistol\shotInterval = 700
	pistol\ammoPerMagazine = 10
	pistol\damage = 5
    pistol\bulletEntity = pistolBullet
	pistol\bulletSpeed = 0.45
	pistol\bulletLifeTime = 450
    pistol\shotSound = soundPistol

    ; Collision Handling
    EntityRadius pistol\bulletEntity, 0.12
    EntityType pistol\bulletEntity, COLLISION_BULLET
    Collisions COLLISION_BULLET, COLLISION_VEHICLE, COLLISION_METHOD_SPHERE_BOX, COLLISION_REACTION_STOP
End Function

Function UpdateAmmo()
    Local bullet.T_Bullet
    For bullet.T_Bullet = Each T_Bullet
        If bullet\lifeTime < ms Then
            FreeEntity bullet\entity
            Delete bullet
        Else
            MoveEntity bullet\entity, 0, 0, bullet\speed
            Local hitVehicle = EntityCollided(bullet\entity, COLLISION_VEHICLE)
            If hitVehicle Then
                ; find vehicle and damage it
                Local car.T_Car
                For car.T_Car = Each T_Car
                    If car\entity = hitVehicle Then
                        car\energy = car\energy - bullet\damage
                        Exit
                    EndIf
                Next

                ; destroy bullet
                FreeEntity bullet\entity
                Delete bullet
            EndIf
        EndIf
    Next
End Function