bombermanCmov
=============

A distributed, multi-player version of the classic Bomberman game for Android devices.

How to compile using eclipse:
-----------------------------
appcompat_v7_2
- First, import both bombermanCmov and libs/appcompat_v7_2 to eclipse.
- Finally, change bombermanCmov's project properties->Android->Libs and add the previously imported appcompat_v7_2.

pt.ist.utl.cmov.wifidirect
- Download link: http://homepages.gsd.inesc-id.pt/~wiki/courses/cmov1314/lab05/ in Exercise II, wdsim-20140330.tgz
- Then, import the CMov-SimWifiP2P-API project to eclipse.
- Finally, change bombermanCmov's project properties->Android->Libs and add the previously imported CMov-SimWifiP2P-API.
- If you get a jar mismatch error (bombermanCmov can't find the appcompat_v7_2 anymore), replace the android-support-v4.jar of the CMov-SimWifiP2P-API with the android-support-v4.jar of the bombermanCmov project.
