Global soundPistol = LoadSound("sfx/weapons/pistol.ogg")
Global soundMachinePistol = LoadSound("sfx/weapons/machine-pistol.ogg")
Global soundMachineGun = LoadSound("sfx/weapons/machine-gun.ogg")
Global soundRifle = LoadSound("sfx/weapons/rifle.ogg")

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
	Field damage#
	Field bulletSpeed#
	Field bulletLifeTime
    Field bulletEntity
    Field shotSound
End Type

Type T_MachinePistol
    Field name$
    Field shotInterval
	Field damage#
	Field bulletSpeed#
	Field bulletLifeTime
    Field bulletEntity
    Field shotSound
End Type

Type T_MachineGun
    Field name$
    Field shotInterval
	Field damage#
	Field bulletSpeed#
	Field bulletLifeTime
    Field bulletEntity
    Field shotSound
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
    Field shotInterval
	Field damage#
	Field bulletSpeed#
	Field bulletLifeTime
    Field bulletEntity
    Field shotSound
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
    ; PISTOL
    Local pistol.T_Pistol = New T_Pistol
    pistol\name = "Pistol"
    pistol\shotInterval = 700
	pistol\damage = 5
    pistol\bulletEntity = pistolBullet
	pistol\bulletSpeed = 0.45
	pistol\bulletLifeTime = 450
    pistol\shotSound = soundPistol

    ; Bullet Collision Handling
    EntityRadius pistol\bulletEntity, 0.12
    EntityType pistol\bulletEntity, COLLISION_BULLET
    Collisions COLLISION_BULLET, COLLISION_VEHICLE, COLLISION_METHOD_SPHERE_BOX, COLLISION_REACTION_STOP

    ; MACHINE PISTOL
    Local machinePistol.T_MachinePistol = New T_MachinePistol
    machinePistol\name = "Machine Pistol"
    machinePistol\shotInterval = 150
	machinePistol\damage = 8
    machinePistol\bulletEntity = pistolBullet
	machinePistol\bulletSpeed = 0.6
	machinePistol\bulletLifeTime = 800
    machinePistol\shotSound = soundMachinePistol

    ; Bullet Collision Handling
    EntityRadius machinePistol\bulletEntity, 0.12
    EntityType machinePistol\bulletEntity, COLLISION_BULLET
    Collisions COLLISION_BULLET, COLLISION_VEHICLE, COLLISION_METHOD_SPHERE_BOX, COLLISION_REACTION_STOP

    ; MACHINE GUN
    Local machineGun.T_MachineGun = New T_MachineGun
    machineGun\name = "Machine Gun"
    machineGun\shotInterval = 200
	machineGun\damage = 17
    machineGun\bulletEntity = pistolBullet
	machineGun\bulletSpeed = 0.6
	machineGun\bulletLifeTime = 800
    machineGun\shotSound = soundMachineGun

    ; Bullet Collision Handling
    EntityRadius machineGun\bulletEntity, 0.12
    EntityType machineGun\bulletEntity, COLLISION_BULLET
    Collisions COLLISION_BULLET, COLLISION_VEHICLE, COLLISION_METHOD_SPHERE_BOX, COLLISION_REACTION_STOP

    ; RIFLE
    Local rifle.T_Rifle = New T_Rifle
    rifle\name = "Rifle"
    rifle\shotInterval = 1600
	rifle\damage = 20
    rifle\bulletEntity = pistolBullet
	rifle\bulletSpeed = 0.8
	rifle\bulletLifeTime = 300
    rifle\shotSound = soundRifle

    ; Bullet Collision Handling
    EntityRadius rifle\bulletEntity, 0.12
    EntityType rifle\bulletEntity, COLLISION_BULLET
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