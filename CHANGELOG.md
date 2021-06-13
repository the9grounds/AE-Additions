
    Changelog of AE Additions.

## Unreleased
### GitLab [2](https://git.the9grounds.com/minecraft/aeadditions.git/issues/2) Rewrite filter handling in export &amp; import bus    *Enhancement*  

**Rewrite filter handling in export & import bus**

 * fixes #2

[a3b45df77cb92e3](https://git.the9grounds.com/minecraft/aeadditions/commit/a3b45df77cb92e3) Aylon Armstrong *2021-06-11 05:04:15*


### GitLab [3](https://git.the9grounds.com/minecraft/aeadditions.git/issues/3) Fix duplication bug with gases    *Bug*  

**Fix gas duplication issue**

 * Fixes #3

[8923e11ab8fe4a1](https://git.the9grounds.com/minecraft/aeadditions/commit/8923e11ab8fe4a1) Aylon Armstrong *2021-06-11 04:46:32*


### GitLab 668   

**ItemFluid does not need Fluid Handler capability. Fixes #668**

 * It&#x27;s just a placeholder item.

[3ed93b4b0793d36](https://git.the9grounds.com/minecraft/aeadditions/commit/3ed93b4b0793d36) Yip Rui Fung *2019-10-19 00:11:17*


### GitLab 669   

**Fix usage of @Optional. Fixes #669**

 * Apparently @Optional.Interface doesn&#x27;t work on scala &#x60;object&#x60;.
 * Also missing @Optional.Method on methods from said interface.

[3b5b3fabb428f09](https://git.the9grounds.com/minecraft/aeadditions/commit/3b5b3fabb428f09) Yip Rui Fung *2019-10-19 00:12:51*


### GitLab 671   

**Update ru_ru.lang (#671)**

 * Update ru_ru.lang

[5dc689cee54a035](https://git.the9grounds.com/minecraft/aeadditions/commit/5dc689cee54a035) Big Energy *2019-10-19 23:17:10*


### GitLab 680   

**Change GasStorageChannel to have the same UnitsPerByte as Liquids**

 * This fixes #680.
 * Default scaling is 8 (like for items), but a Unit of Liquid (and seemingly also for gas) is actally made up out of 1000 milliBuckets.

[7b5f731f64c067e](https://git.the9grounds.com/minecraft/aeadditions/commit/7b5f731f64c067e) Shad0wlife *2020-04-20 19:55:13*


### GitLab 682   

**Update ru_ru.lang (#682)**

 * gas storage tooltips and terminals

[dfbad5f04b53af8](https://git.the9grounds.com/minecraft/aeadditions/commit/dfbad5f04b53af8) Smollet777 *2020-01-16 03:56:47*


### GitLab 683   

**Fix the render error for full drives**

 * Implementation of the solution given in #683.

[fa3d2a52effd250](https://git.the9grounds.com/minecraft/aeadditions/commit/fa3d2a52effd250) Shad0wlife *2020-04-20 19:41:12*


### GitLab 685   

**Solve the parser problem (#685)**


[763bf2079cf6f05](https://git.the9grounds.com/minecraft/aeadditions/commit/763bf2079cf6f05) Rsplwe *2020-01-28 08:49:08*


### GitLab 695   

**Merge pull request #695 from Shad0wlife/fixDriveLEDs**

 * Fix the render error for full drives

[6de58cbeba5faaa](https://git.the9grounds.com/minecraft/aeadditions/commit/6de58cbeba5faaa) ruifung *2020-06-24 05:35:33*


### GitLab 696   

**Merge pull request #696 from Shad0wlife/gasCellSizeAlignment**

 * Change GasStorageChannel to have the same UnitsPerByte as Liquids

[c6d05f7a97f4a34](https://git.the9grounds.com/minecraft/aeadditions/commit/c6d05f7a97f4a34) ruifung *2020-06-24 05:35:06*


### GitLab [9](https://git.the9grounds.com/minecraft/aeadditions.git/issues/9) Export and Import Buses do not show up correctly when hovering over ae2 cards    *Bug*  

**Fix crash, gas import/export bus card tooltip**

 * Fixes #9

[e531cbc3ece466c](https://git.the9grounds.com/minecraft/aeadditions/commit/e531cbc3ece466c) Aylon Armstrong *2021-06-11 08:21:02*


### No issue

**Update changelog.txt**


[30182a230e6d5cb](https://git.the9grounds.com/minecraft/aeadditions/commit/30182a230e6d5cb) MasterYodA *2021-06-13 10:25:10*

**Remove old version properties, drive from environment variables via CI**


[1a265459c1e0305](https://git.the9grounds.com/minecraft/aeadditions/commit/1a265459c1e0305) MasterYodA *2021-06-13 10:14:18*

**Gradle build changes**


[8dba8d06a95c4f7](https://git.the9grounds.com/minecraft/aeadditions/commit/8dba8d06a95c4f7) MasterYodA *2021-06-13 10:13:14*

**Add migration event handlers for extracells > aeadditions & disable open computers**


[cda7aaa36a71774](https://git.the9grounds.com/minecraft/aeadditions/commit/cda7aaa36a71774) MasterYodA *2021-06-13 08:57:21*

**Remove build.sh**


[317a1f772d29e58](https://git.the9grounds.com/minecraft/aeadditions/commit/317a1f772d29e58) MasterYodA *2021-06-13 00:32:13*

**Rename Extra cells to AE Additions**


[20c01ae1c3de6d2](https://git.the9grounds.com/minecraft/aeadditions/commit/20c01ae1c3de6d2) Aylon Armstrong *2021-06-13 00:29:18*

**Merge branch 'configuration' into 'master'**

 * Allow storage cells to be set by config
 * See merge request minecraft/extracells2!2

[fb4dbe885579fb7](https://git.the9grounds.com/minecraft/aeadditions/commit/fb4dbe885579fb7) MasterYodA *2021-06-12 20:45:55*

**Allow storage cells to be set by config**


[4daa820f753954d](https://git.the9grounds.com/minecraft/aeadditions/commit/4daa820f753954d) MasterYodA *2021-06-12 20:45:55*

**Getting rid of deprecated fluid stuff**


[1a23d7a86054b88](https://git.the9grounds.com/minecraft/aeadditions/commit/1a23d7a86054b88) MasterYodA *2021-06-10 10:02:10*

**Merge branch 'new-changes' into 'rv6-1.12'**

 * Getting rid of deprecated fluid stuff
 * See merge request minecraft/extracells2!1

[11c5f50c3c8950f](https://git.the9grounds.com/minecraft/aeadditions/commit/11c5f50c3c8950f) MasterYodA *2021-06-10 10:02:10*

**Remove IGW mod implementation - project is abandoned**


[a23bee00efd0778](https://git.the9grounds.com/minecraft/aeadditions/commit/a23bee00efd0778) Aylon Armstrong *2021-06-08 10:56:06*

**More build changes**


[008860ce13f91a4](https://git.the9grounds.com/minecraft/aeadditions/commit/008860ce13f91a4) Aylon Armstrong *2021-06-08 10:50:26*

**Remove jcenter & local libs**


[1186e9b808c3a49](https://git.the9grounds.com/minecraft/aeadditions/commit/1186e9b808c3a49) Aylon Armstrong *2021-06-08 10:38:06*

**Ender io wrench integration**


[3307028511b448e](https://git.the9grounds.com/minecraft/aeadditions/commit/3307028511b448e) Aylon Armstrong *2021-06-08 10:31:02*

**Add custom cell handler & inventory for larger cell sizes to fix conversion issue**


[511d6c8ba59291c](https://git.the9grounds.com/minecraft/aeadditions/commit/511d6c8ba59291c) Aylon Armstrong *2021-06-08 01:29:06*

**More changes**


[86ecd281dabc820](https://git.the9grounds.com/minecraft/aeadditions/commit/86ecd281dabc820) Aylon Armstrong *2021-06-07 08:28:40*

**Upgrade to newer mcp mapping & fix related code**


[01d73c414e82fd5](https://git.the9grounds.com/minecraft/aeadditions/commit/01d73c414e82fd5) Aylon Armstrong *2021-06-06 23:26:14*

**change to libs**


[6ea0cf08595817c](https://git.the9grounds.com/minecraft/aeadditions/commit/6ea0cf08595817c) Aylon Armstrong *2021-06-06 10:44:33*

**Add artifacts**


[0b03c284cadcb5c](https://git.the9grounds.com/minecraft/aeadditions/commit/0b03c284cadcb5c) Aylon Armstrong *2021-06-06 10:40:00*

**add gradle.properties**


[b515353eca7ecc8](https://git.the9grounds.com/minecraft/aeadditions/commit/b515353eca7ecc8) Aylon Armstrong *2021-06-06 10:30:11*

**Add .gitlab-ci.yml**


[6b5c8419e601cb2](https://git.the9grounds.com/minecraft/aeadditions/commit/6b5c8419e601cb2) MasterYodA *2021-06-06 10:27:53*

**Fix errors & upgrade forge gradle & kotlin**


[ba4c7d552b6e3dc](https://git.the9grounds.com/minecraft/aeadditions/commit/ba4c7d552b6e3dc) Aylon Armstrong *2021-06-06 09:59:16*

**Remove scala files**


[fea445bf30d6818](https://git.the9grounds.com/minecraft/aeadditions/commit/fea445bf30d6818) Aylon Armstrong *2021-06-06 07:15:52*

**Convert scala to kotlin**


[af8070505337d16](https://git.the9grounds.com/minecraft/aeadditions/commit/af8070505337d16) Aylon Armstrong *2021-06-06 07:11:12*

**Merge remote-tracking branch 'origin/rv6-1.12' into rv6-1.12**


[549d87955f9a98f](https://git.the9grounds.com/minecraft/aeadditions/commit/549d87955f9a98f) Yip Rui Fung *2020-01-07 01:54:53*

**Bump version to 2.6.5**

 * More OreDictExporter fixes.

[8ae42f39e43d306](https://git.the9grounds.com/minecraft/aeadditions/commit/8ae42f39e43d306) Yip Rui Fung *2020-01-07 01:50:01*

**Bump version to 2.6.4**


[0ea5d3f1e99f39e](https://git.the9grounds.com/minecraft/aeadditions/commit/0ea5d3f1e99f39e) Yip Rui Fung *2019-10-19 00:13:52*

**Update build script version string.**


[a821c4d71a7b1d6](https://git.the9grounds.com/minecraft/aeadditions/commit/a821c4d71a7b1d6) Yip Rui Fung *2019-10-18 12:53:39*


## 2.6.3
### GitLab 248   

**Fixes $547 and Fixes p455w0rd/WirelessCraftingTerminal#248**


[f2f3e8020538ef1](https://git.the9grounds.com/minecraft/aeadditions/commit/f2f3e8020538ef1) Brock *2018-04-27 11:17:49*


### GitLab 311   

**Merge pull request #631 from BrockWS/fix-wct-311**

 * Fix for p455w0rd/WirelessCraftingTerminal#311

[f76c047e225405b](https://git.the9grounds.com/minecraft/aeadditions/commit/f76c047e225405b) p455w0rd *2019-01-26 01:41:11*

**Fix for p455w0rd/WirelessCraftingTerminal#311**


[26ecbd62f2f6d31](https://git.the9grounds.com/minecraft/aeadditions/commit/26ecbd62f2f6d31) BrockWS *2019-01-08 13:06:54*


### GitLab 469   

**fix #469 on part interface**


[40ebd7973d1113b](https://git.the9grounds.com/minecraft/aeadditions/commit/40ebd7973d1113b) DrummerMC *2017-11-29 18:29:26*

**hopfully fixed #469**


[09f7f195aaccf75](https://git.the9grounds.com/minecraft/aeadditions/commit/09f7f195aaccf75) DrummerMC *2017-11-29 17:24:22*


### GitLab 471   

**fix #471**


[3b6ad4ffd0f7199](https://git.the9grounds.com/minecraft/aeadditions/commit/3b6ad4ffd0f7199) DrummerMC *2017-11-15 16:16:43*


### GitLab 473   

**fix #473**

 * Fixed not saving data

[a7ffc9b540204fe](https://git.the9grounds.com/minecraft/aeadditions/commit/a7ffc9b540204fe) DrummerMC *2017-11-08 18:28:25*


### GitLab 478   

**fix #478**


[433936b3fd7916e](https://git.the9grounds.com/minecraft/aeadditions/commit/433936b3fd7916e) DrummerMC *2017-11-13 14:50:47*


### GitLab 479   

**fix #479**


[3cff964f8095fdb](https://git.the9grounds.com/minecraft/aeadditions/commit/3cff964f8095fdb) DrummerMC *2017-11-13 16:30:31*


### GitLab 480   

**fix #480**


[4f4c2e3381fa1bc](https://git.the9grounds.com/minecraft/aeadditions/commit/4f4c2e3381fa1bc) DrummerMC *2017-11-19 15:53:13*


### GitLab 483   

**fix #483**


[5fa74958d463ca6](https://git.the9grounds.com/minecraft/aeadditions/commit/5fa74958d463ca6) DrummerMC *2017-11-09 19:04:39*


### GitLab 484   

**Work on Gas System**

 * - fix #484

[cf09f72f2a37c98](https://git.the9grounds.com/minecraft/aeadditions/commit/cf09f72f2a37c98) DrummerMC *2017-11-13 12:42:47*


### GitLab 485   

**fix #485**


[96ed7eb760ee734](https://git.the9grounds.com/minecraft/aeadditions/commit/96ed7eb760ee734) DrummerMC *2017-11-19 16:35:36*


### GitLab 486   

**fix #486**


[152af486ab38887](https://git.the9grounds.com/minecraft/aeadditions/commit/152af486ab38887) DrummerMC *2017-11-13 16:09:11*


### GitLab 487   

**fix #487**


[13704f1d8705ed7](https://git.the9grounds.com/minecraft/aeadditions/commit/13704f1d8705ed7) DrummerMC *2017-11-13 16:06:16*


### GitLab 494   

**fix #494**


[7f3cc8fce3b87f3](https://git.the9grounds.com/minecraft/aeadditions/commit/7f3cc8fce3b87f3) DrummerMC *2017-11-15 16:41:58*


### GitLab 495   

**fix #495**

 * Tooltip will not be removed on existing Terminals

[c5693429e135cc7](https://git.the9grounds.com/minecraft/aeadditions/commit/c5693429e135cc7) DrummerMC *2017-11-15 16:15:39*


### GitLab 496   

**hopfully fixed #496**


[6099fc4524c2795](https://git.the9grounds.com/minecraft/aeadditions/commit/6099fc4524c2795) DrummerMC *2017-11-14 19:16:14*

**fix #496**


[af507a0235fd287](https://git.the9grounds.com/minecraft/aeadditions/commit/af507a0235fd287) DrummerMC *2017-11-14 18:14:35*


### GitLab 497   

**fix #497**


[eae875013cc6bb7](https://git.the9grounds.com/minecraft/aeadditions/commit/eae875013cc6bb7) DrummerMC *2017-11-15 14:27:54*


### GitLab 498   

**fix #498**


[b7453cad1d06b5b](https://git.the9grounds.com/minecraft/aeadditions/commit/b7453cad1d06b5b) DrummerMC *2017-11-18 21:38:36*


### GitLab 503   

**fix #503**


[73c51e530d0cccb](https://git.the9grounds.com/minecraft/aeadditions/commit/73c51e530d0cccb) DrummerMC *2017-11-30 17:15:26*


### GitLab 508   

**fix #508**


[3110b7ce0cd7482](https://git.the9grounds.com/minecraft/aeadditions/commit/3110b7ce0cd7482) DrummerMC *2017-12-23 14:47:09*


### GitLab 509   

**Merge pull request #554 from BrockWS/bugfix**

 * Fixed OreDictionary Export Bus. Fixes #539, Fixes #509
 * Fixed Fluid Level Emitter NPE when no fluid is selected. Closes #541

[e2750a5f9106d78](https://git.the9grounds.com/minecraft/aeadditions/commit/e2750a5f9106d78) p455w0rd *2018-04-28 11:46:22*

**Fixed OreDictionary Export Bus. Fixes #539, Fixes #509**


[7d5f2b93dc82d1e](https://git.the9grounds.com/minecraft/aeadditions/commit/7d5f2b93dc82d1e) Brock *2018-04-28 04:41:56*


### GitLab 539   

**Merge pull request #554 from BrockWS/bugfix**

 * Fixed OreDictionary Export Bus. Fixes #539, Fixes #509
 * Fixed Fluid Level Emitter NPE when no fluid is selected. Closes #541

[e2750a5f9106d78](https://git.the9grounds.com/minecraft/aeadditions/commit/e2750a5f9106d78) p455w0rd *2018-04-28 11:46:22*

**Fixed OreDictionary Export Bus. Fixes #539, Fixes #509**


[7d5f2b93dc82d1e](https://git.the9grounds.com/minecraft/aeadditions/commit/7d5f2b93dc82d1e) Brock *2018-04-28 04:41:56*


### GitLab 541   

**Merge pull request #554 from BrockWS/bugfix**

 * Fixed OreDictionary Export Bus. Fixes #539, Fixes #509
 * Fixed Fluid Level Emitter NPE when no fluid is selected. Closes #541

[e2750a5f9106d78](https://git.the9grounds.com/minecraft/aeadditions/commit/e2750a5f9106d78) p455w0rd *2018-04-28 11:46:22*

**Fixed Fluid Level Emitter NPE when no fluid is selected. Closes #541**


[a5f8db7adc447eb](https://git.the9grounds.com/minecraft/aeadditions/commit/a5f8db7adc447eb) Brock *2018-04-28 07:10:16*


### GitLab 547   

**Removed obsolete code, fixes #547**


[580e997b4cc1994](https://git.the9grounds.com/minecraft/aeadditions/commit/580e997b4cc1994) Brock *2018-04-27 12:14:25*


### GitLab 548   

**Removed Bucket Duplication Glitch with Fluid/Gas Wireless Terminals. Fixes #548**


[68045e36e6d861f](https://git.the9grounds.com/minecraft/aeadditions/commit/68045e36e6d861f) Brock *2018-04-27 06:56:57*


### GitLab 550   

**Fixed issue with Gas Storage. Fixes #550**


[675837ddb68ea92](https://git.the9grounds.com/minecraft/aeadditions/commit/675837ddb68ea92) Brock *2018-05-21 10:19:20*


### GitLab 551   

**Fixed Fluid Annihilation Plane stopping if theres no where to put the fluid. Fixes #551**


[e4a023234684533](https://git.the9grounds.com/minecraft/aeadditions/commit/e4a023234684533) Brock *2018-04-27 07:55:54*


### GitLab 553   

**Merge pull request #553 from BrockWS/bugfix**

 * Switched to CF Maven for pwlib, WCT, and Mekanism
 * Removed WCT checkForBooster method (no longer need, this is handled completely on WCT&#x27;s end) [Fixes JEI Autofill with Universal Terminal in Crafting Mode]
 * Fixed Bucket Duplication Glitch in Fluid/Gas Terminals
 * Fixed Annihilation Plane not updating after storage issue was fixed

[24a650915d7054d](https://git.the9grounds.com/minecraft/aeadditions/commit/24a650915d7054d) p455w0rd *2018-04-27 12:30:00*


### GitLab 554   

**Merge pull request #554 from BrockWS/bugfix**

 * Fixed OreDictionary Export Bus. Fixes #539, Fixes #509
 * Fixed Fluid Level Emitter NPE when no fluid is selected. Closes #541

[e2750a5f9106d78](https://git.the9grounds.com/minecraft/aeadditions/commit/e2750a5f9106d78) p455w0rd *2018-04-28 11:46:22*


### GitLab 558   

**Fixed FluidIO not sending the current redstone mode. Fixes #558**


[fbc2abf6a511eaa](https://git.the9grounds.com/minecraft/aeadditions/commit/fbc2abf6a511eaa) Brock *2018-05-10 09:26:29*


### GitLab 559   

**Fixed issue with upgrade_me.requestItems not working with empty slot. Fixes #559**


[3ce1fe777b9f317](https://git.the9grounds.com/minecraft/aeadditions/commit/3ce1fe777b9f317) Brock *2018-06-20 07:16:18*


### GitLab 561   

**Fixes Tooltips. Fixes #561**


[2f6d5431ff04ceb](https://git.the9grounds.com/minecraft/aeadditions/commit/2f6d5431ff04ceb) Brock *2018-05-10 08:58:40*


### GitLab 562   

**Fixed Duplication Glitch with Fluid Interface. Fixes #562**


[5a2b1fe010bb628](https://git.the9grounds.com/minecraft/aeadditions/commit/5a2b1fe010bb628) Brock *2018-05-21 12:30:39*


### GitLab 563   

**Fixed OreDictExportBus again. Fixes #563**


[895b6e19dfd5782](https://git.the9grounds.com/minecraft/aeadditions/commit/895b6e19dfd5782) Brock *2018-05-21 12:14:18*


### GitLab 564   

**Merge pull request #564 from BrockWS/rv5-1.12**

 * Fixed Multiple Issues

[a0ab63f4e86b5b5](https://git.the9grounds.com/minecraft/aeadditions/commit/a0ab63f4e86b5b5) Nedelosk *2018-06-20 08:50:36*


### GitLab 572   

**Merge pull request #572 from BrockWS/rv5-1.12**

 * Updated to AE2 rv6

[1228d743465f8d1](https://git.the9grounds.com/minecraft/aeadditions/commit/1228d743465f8d1) Nedelosk *2018-07-14 20:43:14*


### GitLab 579   

**Merge pull request #579 from BrockWS/rv5-1.12**

 * Added support for ae2 rv6-alpha-4
 * Stuff may or may not break completely with this PR, I&#x27;ll be doing some testing asap along with finishing the full update to AE2 RV6

[ccbe32395b61677](https://git.the9grounds.com/minecraft/aeadditions/commit/ccbe32395b61677) p455w0rd *2018-09-27 21:10:56*


### GitLab 581   

**Wrong Import, Fixes #581**


[a505cc67fa50c05](https://git.the9grounds.com/minecraft/aeadditions/commit/a505cc67fa50c05) Brock *2018-07-18 09:11:24*


### GitLab 591   

**Create ko_kr.lang (#591)**

 * Create ko_kr.lang
 * Update ko_kr.lang
 * Update ko_kr.lang

[6f78b3ed34fc64b](https://git.the9grounds.com/minecraft/aeadditions/commit/6f78b3ed34fc64b) E. Kim *2018-12-12 08:26:00*


### GitLab 605   

**Merge pull request #605 from Arslav/rv6-1.12**

 * Fix broken encoding in ru_ru.lang file

[82378e0196dc83b](https://git.the9grounds.com/minecraft/aeadditions/commit/82378e0196dc83b) ruifung *2018-10-07 07:49:42*


### GitLab 606   

**Fixed Gas Storage Bus Name. Fixes #606**


[d3fc50ee7409140](https://git.the9grounds.com/minecraft/aeadditions/commit/d3fc50ee7409140) BrockWS *2018-12-23 10:06:04*


### GitLab 612   

**AE2 Fluid Term can now be used in UniTerm recipe. Fixes #612**


[8093ec4d6f178fc](https://git.the9grounds.com/minecraft/aeadditions/commit/8093ec4d6f178fc) BrockWS *2018-12-23 10:05:34*


### GitLab 614   

**Fixes #614**

 * Caused by a bug in Scala 2.11. It has been fixed in Scala 2.12 but forge doesn&#x27;t support 2.12.

[db12594726339c5](https://git.the9grounds.com/minecraft/aeadditions/commit/db12594726339c5) BrockWS *2018-12-23 10:04:52*


### GitLab 619   

**Merge pull request #622 from BrockWS/fix-619**

 * Fixed WCT integration and recipe. Fixes #619

[3f5eae665d658ce](https://git.the9grounds.com/minecraft/aeadditions/commit/3f5eae665d658ce) ruifung *2018-12-23 10:09:13*

**Fixed WCT integration and recipe. Fixes #619**


[abe4efb95938eff](https://git.the9grounds.com/minecraft/aeadditions/commit/abe4efb95938eff) BrockWS *2018-12-20 03:02:40*


### GitLab 621   

**Use chestWood for recipe. Fixes #621**


[ec43f73c2bac598](https://git.the9grounds.com/minecraft/aeadditions/commit/ec43f73c2bac598) BrockWS *2018-12-23 10:06:40*


### GitLab 622   

**Merge pull request #622 from BrockWS/fix-619**

 * Fixed WCT integration and recipe. Fixes #619

[3f5eae665d658ce](https://git.the9grounds.com/minecraft/aeadditions/commit/3f5eae665d658ce) ruifung *2018-12-23 10:09:13*


### GitLab 629   

**Merge pull request #629 from bzy-xyz/rv6-1.12-fix-628**

 * fluid cells: reconcile storage math with AE2, other changes

[c5a3169af25dce2](https://git.the9grounds.com/minecraft/aeadditions/commit/c5a3169af25dce2) ruifung *2019-01-08 15:03:27*


### GitLab 631   

**Merge pull request #631 from BrockWS/fix-wct-311**

 * Fix for p455w0rd/WirelessCraftingTerminal#311

[f76c047e225405b](https://git.the9grounds.com/minecraft/aeadditions/commit/f76c047e225405b) p455w0rd *2019-01-26 01:41:11*


### GitLab 633   

**Merge pull request #637 from BrockWS/fix-633**

 * Fixes #633

[bafccc5c0a08aed](https://git.the9grounds.com/minecraft/aeadditions/commit/bafccc5c0a08aed) ruifung *2019-01-28 07:33:24*

**Fixes #633**

 * Caused by WTLib changes.

[7cfcb5671b7880c](https://git.the9grounds.com/minecraft/aeadditions/commit/7cfcb5671b7880c) Brock *2019-01-26 15:51:27*


### GitLab 636   

**Merge pull request #636 from BrockWS/fix-634**

 * Convert to AE2 Cell Handlers

[3a34dbb633d65ae](https://git.the9grounds.com/minecraft/aeadditions/commit/3a34dbb633d65ae) p455w0rd *2019-01-26 10:47:35*


### GitLab 637   

**Merge pull request #637 from BrockWS/fix-633**

 * Fixes #633

[bafccc5c0a08aed](https://git.the9grounds.com/minecraft/aeadditions/commit/bafccc5c0a08aed) ruifung *2019-01-28 07:33:24*


### GitLab 639   

**Merge pull request #639 from roboderpy/crashfix**

 * Do not register items when their mod is not available

[64fba68e11dc20c](https://git.the9grounds.com/minecraft/aeadditions/commit/64fba68e11dc20c) ruifung *2019-02-16 14:22:01*


### GitLab 657   

**Remove usage of Optional.get in UniversalTerminal.scala. Fixes #657.**

 * Do *not* use Optional.get unless it can be 100% sure that it won&#x27;t be empty.

[0c777c246d2441d](https://git.the9grounds.com/minecraft/aeadditions/commit/0c777c246d2441d) Yip Rui Fung *2019-10-18 10:09:22*


### GitLab 667   

**Rewrite ore dict exporting logic. Hopefully fixes #667.**


[40ec6da46ec46a3](https://git.the9grounds.com/minecraft/aeadditions/commit/40ec6da46ec46a3) Yip Rui Fung *2019-10-18 02:46:32*


### Jira alpha-1   

**Updated to ae2 rv6-alpha-1 and added deprecation tooltips for duplicate items**


[1e13c3bd025a77b](https://git.the9grounds.com/minecraft/aeadditions/commit/1e13c3bd025a77b) Brock *2018-07-03 12:06:24*


### Jira alpha-3   

**Added support for ae2 rv6-alpha-3**


[140d5022ff3ff8e](https://git.the9grounds.com/minecraft/aeadditions/commit/140d5022ff3ff8e) Brock *2018-07-15 13:25:22*


### Jira alpha-4   

**Merge pull request #579 from BrockWS/rv5-1.12**

 * Added support for ae2 rv6-alpha-4
 * Stuff may or may not break completely with this PR, I&#x27;ll be doing some testing asap along with finishing the full update to AE2 RV6

[ccbe32395b61677](https://git.the9grounds.com/minecraft/aeadditions/commit/ccbe32395b61677) p455w0rd *2018-09-27 21:10:56*


### Jira fix-619   

**Merge pull request #622 from BrockWS/fix-619**

 * Fixed WCT integration and recipe. Fixes #619

[3f5eae665d658ce](https://git.the9grounds.com/minecraft/aeadditions/commit/3f5eae665d658ce) ruifung *2018-12-23 10:09:13*

**Merge branch 'rv6-1.12' into fix-619**


[74a5dc23c6459be](https://git.the9grounds.com/minecraft/aeadditions/commit/74a5dc23c6459be) ruifung *2018-12-23 10:07:27*


### Jira fix-628   

**Merge pull request #629 from bzy-xyz/rv6-1.12-fix-628**

 * fluid cells: reconcile storage math with AE2, other changes

[c5a3169af25dce2](https://git.the9grounds.com/minecraft/aeadditions/commit/c5a3169af25dce2) ruifung *2019-01-08 15:03:27*


### Jira fix-633   

**Merge pull request #637 from BrockWS/fix-633**

 * Fixes #633

[bafccc5c0a08aed](https://git.the9grounds.com/minecraft/aeadditions/commit/bafccc5c0a08aed) ruifung *2019-01-28 07:33:24*


### Jira fix-634   

**Merge pull request #636 from BrockWS/fix-634**

 * Convert to AE2 Cell Handlers

[3a34dbb633d65ae](https://git.the9grounds.com/minecraft/aeadditions/commit/3a34dbb633d65ae) p455w0rd *2019-01-26 10:47:35*


### Jira stable-39   

**Update build dependencies**

 * - Update mappings to stable-39
 * - Update to gradle 4 for intelliJ idea compatibility
 * - Update AE2WTLib and related libraries.
 * - Update WCT dependency version
 * - Change all extra repositories to use HTTPS.

[971ab6604f51703](https://git.the9grounds.com/minecraft/aeadditions/commit/971ab6604f51703) Yip Rui Fung *2019-10-17 16:19:49*


### Jira wct-311   

**Merge pull request #631 from BrockWS/fix-wct-311**

 * Fix for p455w0rd/WirelessCraftingTerminal#311

[f76c047e225405b](https://git.the9grounds.com/minecraft/aeadditions/commit/f76c047e225405b) p455w0rd *2019-01-26 01:41:11*


### No issue

***Really* fix the OreDict exporter filter parsing now.**

 * Pattern Syntax: [!][@~]&lt;pattern&gt;
 * Filter syntax: &lt;pattern&gt; [&amp;|] &lt;pattern2&gt; [&amp;|] ...
 * Parentheses NOT supported.
 * @ prefix for resource namespace matching (i.e. @minecraft @appliedenergistics)
 * ~ prefix for resource path matching
 * ! for negation (use before @ and ~ if applicable)
 * can be used in any position, although usage of * that isn&#x27;t beginning or end may result in performance hits due to usage of regex matching.
 * Usage with pure oredict matches (no @ and ~) will result in querying the oredict once and building a whitelist that is saved and the filter is only evaluated once.
 * Using @ and ~ will result in the filter being evaluated for every item in AE until it finds a match.
 * Examples tested:
 * @minecraft &amp; ore* (All vanilla ores)
 * @mine* | @thermal* (All blocks from mods with namespaces that starts with mine or thermal)
 * oreGold | oreIron (Gold and iron ore only)

[4f4d41b23f0dbe7](https://git.the9grounds.com/minecraft/aeadditions/commit/4f4d41b23f0dbe7) Yip Rui Fung *2019-10-18 12:18:50*

**Fix compile errors resulting from updating mappings to stable_39**


[1f78a0b5c15a64e](https://git.the9grounds.com/minecraft/aeadditions/commit/1f78a0b5c15a64e) Yip Rui Fung *2019-10-18 02:45:37*

**Do not register items when their mod is not available**


[5050f0161634b36](https://git.the9grounds.com/minecraft/aeadditions/commit/5050f0161634b36) DBotThePony *2019-02-16 10:53:35*

**Fix wrong annotation**


[6749467573b7151](https://git.the9grounds.com/minecraft/aeadditions/commit/6749467573b7151) Brock *2019-01-26 10:33:14*

**Convert to AE2 Cell Handlers**


[55e5e99ad803a7f](https://git.the9grounds.com/minecraft/aeadditions/commit/55e5e99ad803a7f) Brock *2019-01-26 10:07:09*

**fluid cells: reconcile storage math with AE2, other changes**

 * Current implementation of fluid cells uses a hardcoded 250/byte ratio, which
 * in general may not correspond with the actual itemsPerByte for the rest of
 * AE2. Additionally, bytes per type are ignored.
 * This change pulls the actual itemsPerByte from the underlying AE2 channel
 * type, and makes types consume bytes at the same ratio as in AE2.
 * As a side effect, some misuse of &quot;bytes&quot; as &quot;items&quot; was corrected for fluid
 * cells (reconciling these with the AE2 behaviour) to avoid an integer overflow.
 * Additionally, some changes are made to gas cells to avoid UI confusion, but
 * the underlying mechanics of these cells are untouched as they are outside
 * scope for the issue being addressed.

[4261c1a5d371ac9](https://git.the9grounds.com/minecraft/aeadditions/commit/4261c1a5d371ac9) Ben Z Yuan *2019-01-07 07:23:20*

**Update README.md build status**

 * The previous AppVeyor build status is for the rv5 branch, this now points to the travis CI project used for github tests as that is more up to date.

[7cdd2acefdb0251](https://git.the9grounds.com/minecraft/aeadditions/commit/7cdd2acefdb0251) ruifung *2018-12-23 10:18:08*

**Fix broken encoding in ru_ru.lang file**


[fdee881a373f025](https://git.the9grounds.com/minecraft/aeadditions/commit/fdee881a373f025) Vyacheslav Ardesov *2018-10-06 17:56:35*

**Fixed ae2 api changes**


[58944fe595f5e23](https://git.the9grounds.com/minecraft/aeadditions/commit/58944fe595f5e23) Brock *2018-09-23 06:57:23*

**Deprecated Fluid Level Emitter and Fluid Planes**


[4cf8e29c5c41c35](https://git.the9grounds.com/minecraft/aeadditions/commit/4cf8e29c5c41c35) Brock *2018-07-09 07:16:38*

**Fixed SMP tooltip issue**


[06648f98a4a18eb](https://git.the9grounds.com/minecraft/aeadditions/commit/06648f98a4a18eb) Brock *2018-07-07 06:41:21*

**Removed Deprecated item recipes and added conversion recipes**


[3eb700dce008e06](https://git.the9grounds.com/minecraft/aeadditions/commit/3eb700dce008e06) Brock *2018-07-03 13:17:28*

**Bumped version**


[8a5160ffdf7c950](https://git.the9grounds.com/minecraft/aeadditions/commit/8a5160ffdf7c950) Brock *2018-04-28 08:01:12*

**bump version**


[d5e742349ced6b6](https://git.the9grounds.com/minecraft/aeadditions/commit/d5e742349ced6b6) p455w0rd *2018-04-27 12:46:22*

**Bumped Forge Version**


[8bc93620bdc921a](https://git.the9grounds.com/minecraft/aeadditions/commit/8bc93620bdc921a) Brock *2018-04-26 13:28:40*

**Use CurseForge Maven instead of getting files directly from CurseCDN/ForgeCDN**


[2d481d325612abf](https://git.the9grounds.com/minecraft/aeadditions/commit/2d481d325612abf) Brock *2018-04-26 12:38:10*

**added getChannel to ItemStorageCellPhysical.java**


[9605a25f0159bf5](https://git.the9grounds.com/minecraft/aeadditions/commit/9605a25f0159bf5) DrummerMC *2018-01-18 16:15:25*

**fixed Certus Tank rendering**


[f7e68afcc6a9693](https://git.the9grounds.com/minecraft/aeadditions/commit/f7e68afcc6a9693) DrummerMC *2017-11-27 15:05:09*

**Add IGW, BC and COFH Hammer integration**


[5d00b1ad7ed4e97](https://git.the9grounds.com/minecraft/aeadditions/commit/5d00b1ad7ed4e97) DrummerMC *2017-11-27 15:04:25*

**fixed OpenComputers Integration**


[a98367c60a1244c](https://git.the9grounds.com/minecraft/aeadditions/commit/a98367c60a1244c) DrummerMC *2017-11-27 15:02:22*

**fixed FluidStorageBus duplicate Fluids**


[2bbec6e21707be8](https://git.the9grounds.com/minecraft/aeadditions/commit/2bbec6e21707be8) DrummerMC *2017-11-27 15:01:16*

**fixed JEI Recipe for universal terminal**


[9d065eabf131b9c](https://git.the9grounds.com/minecraft/aeadditions/commit/9d065eabf131b9c) DrummerMC *2017-11-25 14:08:12*

**fixed crash on startup if OpenComputers is not installed**


[d88f5909f63c8da](https://git.the9grounds.com/minecraft/aeadditions/commit/d88f5909f63c8da) DrummerMC *2017-11-24 18:13:38*

**fix 502**

 * - fixed crash on startup with old OpenComputers version

[54789e689d99197](https://git.the9grounds.com/minecraft/aeadditions/commit/54789e689d99197) DrummerMC *2017-11-24 16:50:31*

**Work on recipes**


[466ca07b25048d1](https://git.the9grounds.com/minecraft/aeadditions/commit/466ca07b25048d1) DrummerMC *2017-11-24 16:16:45*

**Re-add OC Integration**


[f9bd792f50baa33](https://git.the9grounds.com/minecraft/aeadditions/commit/f9bd792f50baa33) DrummerMC *2017-11-24 15:00:34*

**fixed autocrafting**


[d9e59a4205f7cf2](https://git.the9grounds.com/minecraft/aeadditions/commit/d9e59a4205f7cf2) DrummerMC *2017-11-19 15:54:23*

**ore dict export bus now suports IItemHandler**


[4bfe7fc1fac6600](https://git.the9grounds.com/minecraft/aeadditions/commit/4bfe7fc1fac6600) DrummerMC *2017-11-19 15:53:53*

**Update version**


[a1bf92a1b459480](https://git.the9grounds.com/minecraft/aeadditions/commit/a1bf92a1b459480) DrummerMC *2017-11-18 21:38:52*

**Update build.gradle**


[d5ae70e0bae9a7e](https://git.the9grounds.com/minecraft/aeadditions/commit/d5ae70e0bae9a7e) DrummerMC *2017-11-18 17:35:00*

**Add Curseforge upload**


[3f8ecd1ff67e37b](https://git.the9grounds.com/minecraft/aeadditions/commit/3f8ecd1ff67e37b) DrummerMC *2017-11-18 16:55:39*

**remove deobf jar and add api jar**


[c2b80c62ad8406e](https://git.the9grounds.com/minecraft/aeadditions/commit/c2b80c62ad8406e) DrummerMC *2017-11-16 09:34:06*

**Removed LayerTubeConnection and LayerGasHandler**


[7ed84314b816c11](https://git.the9grounds.com/minecraft/aeadditions/commit/7ed84314b816c11) DrummerMC *2017-11-15 19:39:06*

**WAILA on CertusTank now shows the complete Tank**


[aa669e98d101386](https://git.the9grounds.com/minecraft/aeadditions/commit/aa669e98d101386) DrummerMC *2017-11-15 16:14:50*

**Update build.gradle**


[ae512f96bf54ce6](https://git.the9grounds.com/minecraft/aeadditions/commit/ae512f96bf54ce6) DrummerMC *2017-11-14 15:45:12*

**Update build.properties**


[492314e7980c80a](https://git.the9grounds.com/minecraft/aeadditions/commit/492314e7980c80a) DrummerMC *2017-11-14 13:18:10*

**Update ContainerStorage.java**


[b0b643d53724425](https://git.the9grounds.com/minecraft/aeadditions/commit/b0b643d53724425) DrummerMC *2017-11-13 16:42:29*

**updateGui only on Client**


[59f491efd12e485](https://git.the9grounds.com/minecraft/aeadditions/commit/59f491efd12e485) DrummerMC *2017-11-13 16:39:23*

**Update Wireless Crafting Terminal**


[2ab8b6f230b1143](https://git.the9grounds.com/minecraft/aeadditions/commit/2ab8b6f230b1143) DrummerMC *2017-11-13 16:31:49*

**bug fixes**


[50c596ec747bfec](https://git.the9grounds.com/minecraft/aeadditions/commit/50c596ec747bfec) DrummerMC *2017-11-13 16:30:55*

**Added Wireless Crafting Terminal**


[0ff4b2c0bdb8426](https://git.the9grounds.com/minecraft/aeadditions/commit/0ff4b2c0bdb8426) DrummerMC *2017-11-13 13:47:51*

**Update build.gradle**


[fda8e34888cf2f4](https://git.the9grounds.com/minecraft/aeadditions/commit/fda8e34888cf2f4) DrummerMC *2017-11-09 17:51:40*

**fixed ore dict export bus**


[7a3f4cf0658cbd0](https://git.the9grounds.com/minecraft/aeadditions/commit/7a3f4cf0658cbd0) DrummerMC *2017-11-08 18:44:38*


## 8
### GitLab [1](https://git.the9grounds.com/minecraft/aeadditions.git/issues/1) Allow Storage Cell sizes to be set via config    *Enhancement*  

**Merge pull request #1 from M3gaFr3ak/rv2**

 * Rv2

[756942058333bb7](https://git.the9grounds.com/minecraft/aeadditions/commit/756942058333bb7) Alheimerjung *2015-04-07 15:42:04*


### GitLab 151   

**Merge pull request #211 from RAnders00/patch-1**

 * PR what is suggested in #151

[233ef825afa44f8](https://git.the9grounds.com/minecraft/aeadditions/commit/233ef825afa44f8) DrummerMC *2015-05-10 10:36:40*

**PR what is suggested in #151**

 * Change inventory.usedTypes() &#x3D;&#x3D; inventory.totalBytes() to inventory.usedTypes() &#x3D;&#x3D; inventory.totalTypes()

[742e76a34a63213](https://git.the9grounds.com/minecraft/aeadditions/commit/742e76a34a63213) RAnders00 *2015-05-01 18:18:42*


### GitLab 173   

**fixed #173**


[47d5f2718931718](https://git.the9grounds.com/minecraft/aeadditions/commit/47d5f2718931718) DrummerMC *2015-02-16 00:13:20*


### GitLab 176   

**EC 2.2.55**

 * - fixed #176

[189477ca4e8c753](https://git.the9grounds.com/minecraft/aeadditions/commit/189477ca4e8c753) DrummerMC *2015-02-20 17:12:50*


### GitLab 179   

**EC 2.2.61**

 * - fixed #179
 * - fixed #180
 * - fixed performance problem

[42633e725d7abf9](https://git.the9grounds.com/minecraft/aeadditions/commit/42633e725d7abf9) DrummerMC *2015-03-25 12:50:50*


### GitLab 180   

**EC 2.2.61**

 * - fixed #179
 * - fixed #180
 * - fixed performance problem

[42633e725d7abf9](https://git.the9grounds.com/minecraft/aeadditions/commit/42633e725d7abf9) DrummerMC *2015-03-25 12:50:50*


### GitLab 189   

**Merge pull request #189 from bakaxyf/rv2**

 * Update zh_CN.lang

[5376cc3f292f985](https://git.the9grounds.com/minecraft/aeadditions/commit/5376cc3f292f985) DrummerMC *2015-04-07 09:04:10*


### GitLab 193   

**Merge pull request #193 from Alheimerjung/rv2**

 * Added Crafting Recipe for Fluid Vibran Chamber

[f95df124cb4b342](https://git.the9grounds.com/minecraft/aeadditions/commit/f95df124cb4b342) DrummerMC *2015-04-07 21:40:31*


### GitLab 194   

**Work on blast resistand me drive**

 * - Fixed #194
 * - Work on rendering
 * - Added recipes

[d1727c545f79554](https://git.the9grounds.com/minecraft/aeadditions/commit/d1727c545f79554) DrummerMC *2015-04-08 17:28:01*


### GitLab 195   

**Update Forge**

 * - fixed #195

[32050e24347d2a8](https://git.the9grounds.com/minecraft/aeadditions/commit/32050e24347d2a8) DrummerMC *2015-04-11 16:17:59*


### GitLab 196   

**Merge pull request #196 from bakaxyf/rv2**

 * Update zh_CN.lang

[9e1a53a1c2ae1cf](https://git.the9grounds.com/minecraft/aeadditions/commit/9e1a53a1c2ae1cf) DrummerMC *2015-04-10 11:30:47*


### GitLab 203   

**Merge pull request #203 from Adaptivity/rv2**

 * Update ru_RU.lang

[824f62abd40f0ab](https://git.the9grounds.com/minecraft/aeadditions/commit/824f62abd40f0ab) Leon Loeser *2015-04-18 18:24:53*


### GitLab 206   

**Merge pull request #206 from bakaxyf/rv2**

 * Update zh_CN.lang

[c41e6404a4d0a70](https://git.the9grounds.com/minecraft/aeadditions/commit/c41e6404a4d0a70) DrummerMC *2015-04-20 18:46:17*


### GitLab 211   

**Merge pull request #211 from RAnders00/patch-1**

 * PR what is suggested in #151

[233ef825afa44f8](https://git.the9grounds.com/minecraft/aeadditions/commit/233ef825afa44f8) DrummerMC *2015-05-10 10:36:40*


### GitLab 218   

**Merge pull request #218 from ibmibmibm/rv2**

 * Update zh_TW.lang

[cf00ad0b9888d42](https://git.the9grounds.com/minecraft/aeadditions/commit/cf00ad0b9888d42) DrummerMC *2015-05-10 10:36:22*


### GitLab 227   

**Merge pull request #227 from halvors/rv2**

 * Updated gradle to version 2.4

[9d6da6e0fbff91b](https://git.the9grounds.com/minecraft/aeadditions/commit/9d6da6e0fbff91b) DrummerMC *2015-05-26 17:01:51*


### GitLab 230   

**Merge pull request #230 from XxCoolGamesxX/rv2**

 * Add the &quot;es_ES.lang&quot;

[20e2c1c68269695](https://git.the9grounds.com/minecraft/aeadditions/commit/20e2c1c68269695) DrummerMC *2015-05-31 20:03:19*


### GitLab 231   

**Make blast resistend me drive WIP**

 * #231

[fc00d1f3b54d710](https://git.the9grounds.com/minecraft/aeadditions/commit/fc00d1f3b54d710) DrummerMC *2015-06-09 21:09:32*


### GitLab 240   

**Update OC, fix #240**


[427e931ac2097d7](https://git.the9grounds.com/minecraft/aeadditions/commit/427e931ac2097d7) DrummerMC *2015-07-05 10:45:19*


### GitLab 262   

**EC 2.2.74**

 * Update AE
 * Fixed #265, #262

[1f35cb849938aab](https://git.the9grounds.com/minecraft/aeadditions/commit/1f35cb849938aab) DrummerMC *2015-08-02 14:38:59*


### GitLab 265   

**EC 2.2.74**

 * Update AE
 * Fixed #265, #262

[1f35cb849938aab](https://git.the9grounds.com/minecraft/aeadditions/commit/1f35cb849938aab) DrummerMC *2015-08-02 14:38:59*


### GitLab 270   

**Merge pull request #270 from ruifung/rv2**

 * Do not equal trade blocks with negative hardness.

[e6d7c4759729ff2](https://git.the9grounds.com/minecraft/aeadditions/commit/e6d7c4759729ff2) DrummerMC *2015-08-16 10:14:42*


### GitLab 271   

**Make containers for some parts / blocks close when the tile entity they are associated with is no longer present in the world.**

 * Should fix #271

[98e85796850a789](https://git.the9grounds.com/minecraft/aeadditions/commit/98e85796850a789) Yip Rui Fung *2015-08-20 01:05:19*


### GitLab 282   

**Added null check so fluids which return null for getBlock doesn't crash game. Fixes #282**


[25ebaa0b18c6423](https://git.the9grounds.com/minecraft/aeadditions/commit/25ebaa0b18c6423) ruifung *2015-08-20 07:48:24*


### GitLab 287   

**Hopefully the last commit to fix my derpiness when fixing #287**

 * Fixes crash due to my derp.

[ed4dc202be86af2](https://git.the9grounds.com/minecraft/aeadditions/commit/ed4dc202be86af2) ruifung *2015-09-11 09:27:17*

**Parts should drop contents of inventories when broken. Fixes #287**


[34a87cf05c10838](https://git.the9grounds.com/minecraft/aeadditions/commit/34a87cf05c10838) ruifung *2015-09-03 05:11:40*


### GitLab 296   

**fix #296**


[ccfc33d12fc1c35](https://git.the9grounds.com/minecraft/aeadditions/commit/ccfc33d12fc1c35) __Enderlord__ *2017-11-06 18:55:06*

**fix #296**


[7c51481aba549b2](https://git.the9grounds.com/minecraft/aeadditions/commit/7c51481aba549b2) DrummerMC *2015-11-29 15:25:42*

**Merge pull request #297 from AmethystAir/rv2**

 * Fixes excessive power drain from capacitors Fixes #296

[89612a273d2dfa8](https://git.the9grounds.com/minecraft/aeadditions/commit/89612a273d2dfa8) ruifung *2015-09-24 00:27:02*


### GitLab 297   

**Merge pull request #297 from AmethystAir/rv2**

 * Fixes excessive power drain from capacitors Fixes #296

[89612a273d2dfa8](https://git.the9grounds.com/minecraft/aeadditions/commit/89612a273d2dfa8) ruifung *2015-09-24 00:27:02*


### GitLab 302   

**Fixed Wireless Fluid Terminal dupe**

 * fix #302

[54a52884657c1e3](https://git.the9grounds.com/minecraft/aeadditions/commit/54a52884657c1e3) DrummerMC *2015-12-25 11:37:49*


### GitLab 314   

**Fix #314**


[e2d6af142a156a2](https://git.the9grounds.com/minecraft/aeadditions/commit/e2d6af142a156a2) DrummerMC *2015-11-29 11:41:05*


### GitLab 321   

**Fix #321**


[7d33908447e08ab](https://git.the9grounds.com/minecraft/aeadditions/commit/7d33908447e08ab) DrummerMC *2015-11-29 11:15:36*


### GitLab 322   

**Merge pull request #322 from unascribed/patch-1**

 * Fix &quot;Already tesselating!&quot; error with the fluid storage monitor

[495f0ba11ecf4d2](https://git.the9grounds.com/minecraft/aeadditions/commit/495f0ba11ecf4d2) DrummerMC *2015-11-26 14:29:56*


### GitLab 326   

**Fixed #326**


[f6cdb7f97a37df8](https://git.the9grounds.com/minecraft/aeadditions/commit/f6cdb7f97a37df8) DrummerMC *2015-11-28 13:24:10*


### GitLab 327   

**Merge pull request #327 from XFRGod/patch-1**

 * Create hu_HU.lang

[a7d248da97d5947](https://git.the9grounds.com/minecraft/aeadditions/commit/a7d248da97d5947) DrummerMC *2015-11-28 17:15:03*


### GitLab 329   

**Finished Universal and Gas Terminal, Bug fixes Part 3**

 * fix #344
 * fix #329

[51162a63aa539dc](https://git.the9grounds.com/minecraft/aeadditions/commit/51162a63aa539dc) DrummerMC *2015-12-17 20:58:57*

**Finished Universal and Gas Terminal, Bug fixes Part 2**

 * fix #344
 * fix #329

[9d55057918cf008](https://git.the9grounds.com/minecraft/aeadditions/commit/9d55057918cf008) DrummerMC *2015-12-17 20:57:19*

**Finisched Gas and Universal Terminal, Bug Fixes**

 * fix #344
 * fix #329

[045cb3510ffbe67](https://git.the9grounds.com/minecraft/aeadditions/commit/045cb3510ffbe67) DrummerMC *2015-12-17 20:56:34*


### GitLab 330   

**Merge pull request #330 from XFRGod/patch-3**

 * Update hu_HU.lang

[c1d8285fc6ad11c](https://git.the9grounds.com/minecraft/aeadditions/commit/c1d8285fc6ad11c) DrummerMC *2015-11-30 14:43:39*


### GitLab 331   

**Fix #331**


[ebd1a2b111e92e1](https://git.the9grounds.com/minecraft/aeadditions/commit/ebd1a2b111e92e1) DrummerMC *2015-11-30 16:15:18*


### GitLab 332   

**Fix #332 Block Container Dupe bug**


[eb2df21770d92e0](https://git.the9grounds.com/minecraft/aeadditions/commit/eb2df21770d92e0) Nedelosk *2017-10-09 20:22:11*


### GitLab 333   

**Dont show Gas Items if gas is disabled and bug fixes**

 * fix #333
 * fix #343

[b58e1657a556ef5](https://git.the9grounds.com/minecraft/aeadditions/commit/b58e1657a556ef5) DrummerMC *2015-12-13 17:20:42*


### GitLab 335   

**Merge pull request #335 from XFRGod/patch-5**

 * Update hu_HU.lang

[c3ee9ea64645eb6](https://git.the9grounds.com/minecraft/aeadditions/commit/c3ee9ea64645eb6) DrummerMC *2015-11-30 20:35:23*


### GitLab 341   

**Added Wireless gas terminal and bug fixes**

 * - Fix #341

[b9ac3465f781e40](https://git.the9grounds.com/minecraft/aeadditions/commit/b9ac3465f781e40) DrummerMC *2015-12-12 17:32:37*


### GitLab 342   

**Merge pull request #342 from 3TUSK/rv2**

 * Update zh_CN.lang

[b36c3e3fe540e7b](https://git.the9grounds.com/minecraft/aeadditions/commit/b36c3e3fe540e7b) DrummerMC *2015-12-11 15:43:15*


### GitLab 343   

**Dont show Gas Items if gas is disabled and bug fixes**

 * fix #333
 * fix #343

[b58e1657a556ef5](https://git.the9grounds.com/minecraft/aeadditions/commit/b58e1657a556ef5) DrummerMC *2015-12-13 17:20:42*


### GitLab 344   

**Finished Universal and Gas Terminal, Bug fixes Part 3**

 * fix #344
 * fix #329

[51162a63aa539dc](https://git.the9grounds.com/minecraft/aeadditions/commit/51162a63aa539dc) DrummerMC *2015-12-17 20:58:57*

**Finished Universal and Gas Terminal, Bug fixes Part 2**

 * fix #344
 * fix #329

[9d55057918cf008](https://git.the9grounds.com/minecraft/aeadditions/commit/9d55057918cf008) DrummerMC *2015-12-17 20:57:19*

**Finisched Gas and Universal Terminal, Bug Fixes**

 * fix #344
 * fix #329

[045cb3510ffbe67](https://git.the9grounds.com/minecraft/aeadditions/commit/045cb3510ffbe67) DrummerMC *2015-12-17 20:56:34*


### GitLab 348   

**EC 2.3.5**

 * - fix #349
 * - fix #348

[200d85d27fcd794](https://git.the9grounds.com/minecraft/aeadditions/commit/200d85d27fcd794) DrummerMC *2015-12-18 16:53:53*


### GitLab 349   

**EC 2.3.5**

 * - fix #349
 * - fix #348

[200d85d27fcd794](https://git.the9grounds.com/minecraft/aeadditions/commit/200d85d27fcd794) DrummerMC *2015-12-18 16:53:53*


### GitLab 350   

**Fixed Terminals on Multiplayer**

 * fix #350

[dc45028798ecee5](https://git.the9grounds.com/minecraft/aeadditions/commit/dc45028798ecee5) DrummerMC *2015-12-25 11:36:25*


### GitLab 352   

**Fixed Server Crash**

 * fix #354
 * fix #352

[ea77ca6f1978905](https://git.the9grounds.com/minecraft/aeadditions/commit/ea77ca6f1978905) DrummerMC *2015-12-25 11:35:18*


### GitLab 353   

**Remove Console Spam**

 * fix #353

[b1e2f35452280ea](https://git.the9grounds.com/minecraft/aeadditions/commit/b1e2f35452280ea) DrummerMC *2015-12-25 11:32:33*


### GitLab 354   

**Fixed Server Crash**

 * fix #354
 * fix #352

[ea77ca6f1978905](https://git.the9grounds.com/minecraft/aeadditions/commit/ea77ca6f1978905) DrummerMC *2015-12-25 11:35:18*


### GitLab 356   

**Fix #356**


[b9f8d7137b82e33](https://git.the9grounds.com/minecraft/aeadditions/commit/b9f8d7137b82e33) DrummerMC *2016-12-28 12:37:40*


### GitLab 358   

**Added more Gas Stuff**

 * close #358

[ab5a389354e77c3](https://git.the9grounds.com/minecraft/aeadditions/commit/ab5a389354e77c3) DrummerMC *2016-12-29 13:37:51*


### GitLab 360   

**Merge pull request #360 from Honon/patch-1**

 * Japanese translation files

[d9a943db282de31](https://git.the9grounds.com/minecraft/aeadditions/commit/d9a943db282de31) DrummerMC *2016-12-29 13:31:02*


### GitLab 372   

**fix #372**


[e24a1e90c45674e](https://git.the9grounds.com/minecraft/aeadditions/commit/e24a1e90c45674e) DrummerMC *2016-01-31 12:58:45*


### GitLab 382   

**Merge pull request #382 from xLivan/xLivan-patch-1**

 * Fix bedrock breaking in 3*3 mode

[02f9815b9ee5284](https://git.the9grounds.com/minecraft/aeadditions/commit/02f9815b9ee5284) DrummerMC *2016-03-14 18:13:35*


### GitLab 384   

**Fix #384 Cards stored in Network Tool aren't drawn in correct locations**


[ec8a11c3eed5e44](https://git.the9grounds.com/minecraft/aeadditions/commit/ec8a11c3eed5e44) Nedelosk *2017-10-09 20:10:54*


### GitLab 389   

**Fix #389 Broken Fluid Level Emitter and Fix #458 Error in build.gradle**


[9d9126acd9699c3](https://git.the9grounds.com/minecraft/aeadditions/commit/9d9126acd9699c3) Nedelosk *2017-10-09 16:28:34*


### GitLab 391   

**fix #391, #397**


[6e844d7c59384c9](https://git.the9grounds.com/minecraft/aeadditions/commit/6e844d7c59384c9) DrummerMC *2016-05-28 15:04:15*


### GitLab 397   

**fix #391, #397**


[6e844d7c59384c9](https://git.the9grounds.com/minecraft/aeadditions/commit/6e844d7c59384c9) DrummerMC *2016-05-28 15:04:15*


### GitLab 401   

**Merge pull request #401 from yaroslav4167/patch-1**

 * Update BlockCertusTank.java

[a4c497d0ffa113e](https://git.the9grounds.com/minecraft/aeadditions/commit/a4c497d0ffa113e) DrummerMC *2016-05-30 14:56:44*


### GitLab 403   

**Fix #403 Gas Storage Gui is broken**


[e70a413b2f6ac83](https://git.the9grounds.com/minecraft/aeadditions/commit/e70a413b2f6ac83) Nedelosk *2017-10-10 14:28:52*


### GitLab 404   

**EC 2.3.14**

 * fix #404

[b6b79468bdc5f47](https://git.the9grounds.com/minecraft/aeadditions/commit/b6b79468bdc5f47) DrummerMC *2016-06-17 15:10:16*


### GitLab 406   

**Merge pull request #406 from yaroslav4167/patch-2**

 * Update BlockCertusTank.java

[6e5742cb054de4c](https://git.the9grounds.com/minecraft/aeadditions/commit/6e5742cb054de4c) DrummerMC *2016-11-01 10:46:08*


### GitLab 420   

**Merge pull request #420 from LorenzoDCC/patch-1**

 * Portuguese translation

[b456f585fa9f8b7](https://git.the9grounds.com/minecraft/aeadditions/commit/b456f585fa9f8b7) DrummerMC *2016-11-01 10:46:21*


### GitLab 423   

**Fix #423 Fluid Formation Pane comparing in world block to Blocks.air instead of Block.isAir**


[e2b3cc87e0feb23](https://git.the9grounds.com/minecraft/aeadditions/commit/e2b3cc87e0feb23) Nedelosk *2017-10-09 19:58:34*


### GitLab 444   

**Fix #444 Incorrect subtraction in Fluid Interface**


[b378b1755e9ab1f](https://git.the9grounds.com/minecraft/aeadditions/commit/b378b1755e9ab1f) Nedelosk *2017-10-09 00:45:42*


### GitLab 455   

**Fix #455 Broken Fluid / Gas Terminal**


[c3baeeead2620d1](https://git.the9grounds.com/minecraft/aeadditions/commit/c3baeeead2620d1) Nedelosk *2017-10-09 00:30:19*


### GitLab 458   

**Fix #389 Broken Fluid Level Emitter and Fix #458 Error in build.gradle**


[9d9126acd9699c3](https://git.the9grounds.com/minecraft/aeadditions/commit/9d9126acd9699c3) Nedelosk *2017-10-09 16:28:34*


### GitLab 461   

**fix #461**


[2445b717da848b3](https://git.the9grounds.com/minecraft/aeadditions/commit/2445b717da848b3) DrummerMC *2017-10-31 17:18:15*


### GitLab 466   

**fix #466**


[5f4545e6023cdfe](https://git.the9grounds.com/minecraft/aeadditions/commit/5f4545e6023cdfe) DrummerMC *2017-11-05 12:18:02*


### GitLab 470   

**fix #470**


[a704bdc1c097095](https://git.the9grounds.com/minecraft/aeadditions/commit/a704bdc1c097095) DrummerMC *2017-11-07 17:19:50*


### GitLab 475   

**fix #475**


[f310e5f89b9e416](https://git.the9grounds.com/minecraft/aeadditions/commit/f310e5f89b9e416) DrummerMC *2017-11-08 16:00:18*


### GitLab 476   

**fix #476**


[72488c5f064a577](https://git.the9grounds.com/minecraft/aeadditions/commit/72488c5f064a577) DrummerMC *2017-11-08 16:15:26*


### GitLab 477   

**fix #477**


[64d6ba57e2e9d88](https://git.the9grounds.com/minecraft/aeadditions/commit/64d6ba57e2e9d88) DrummerMC *2017-11-08 16:24:40*


### Jira patch-1   

**Merge pull request #420 from LorenzoDCC/patch-1**

 * Portuguese translation

[b456f585fa9f8b7](https://git.the9grounds.com/minecraft/aeadditions/commit/b456f585fa9f8b7) DrummerMC *2016-11-01 10:46:21*

**Merge pull request #401 from yaroslav4167/patch-1**

 * Update BlockCertusTank.java

[a4c497d0ffa113e](https://git.the9grounds.com/minecraft/aeadditions/commit/a4c497d0ffa113e) DrummerMC *2016-05-30 14:56:44*

**Merge pull request #382 from xLivan/xLivan-patch-1**

 * Fix bedrock breaking in 3*3 mode

[02f9815b9ee5284](https://git.the9grounds.com/minecraft/aeadditions/commit/02f9815b9ee5284) DrummerMC *2016-03-14 18:13:35*

**Merge pull request #360 from Honon/patch-1**

 * Japanese translation files

[d9a943db282de31](https://git.the9grounds.com/minecraft/aeadditions/commit/d9a943db282de31) DrummerMC *2016-12-29 13:31:02*

**Merge pull request #327 from XFRGod/patch-1**

 * Create hu_HU.lang

[a7d248da97d5947](https://git.the9grounds.com/minecraft/aeadditions/commit/a7d248da97d5947) DrummerMC *2015-11-28 17:15:03*

**Merge pull request #322 from unascribed/patch-1**

 * Fix &quot;Already tesselating!&quot; error with the fluid storage monitor

[495f0ba11ecf4d2](https://git.the9grounds.com/minecraft/aeadditions/commit/495f0ba11ecf4d2) DrummerMC *2015-11-26 14:29:56*

**Merge pull request #211 from RAnders00/patch-1**

 * PR what is suggested in #151

[233ef825afa44f8](https://git.the9grounds.com/minecraft/aeadditions/commit/233ef825afa44f8) DrummerMC *2015-05-10 10:36:40*


### Jira patch-2   

**Merge pull request #406 from yaroslav4167/patch-2**

 * Update BlockCertusTank.java

[6e5742cb054de4c](https://git.the9grounds.com/minecraft/aeadditions/commit/6e5742cb054de4c) DrummerMC *2016-11-01 10:46:08*


### Jira patch-3   

**Merge pull request #330 from XFRGod/patch-3**

 * Update hu_HU.lang

[c1d8285fc6ad11c](https://git.the9grounds.com/minecraft/aeadditions/commit/c1d8285fc6ad11c) DrummerMC *2015-11-30 14:43:39*


### Jira patch-5   

**Merge pull request #335 from XFRGod/patch-5**

 * Update hu_HU.lang

[c3ee9ea64645eb6](https://git.the9grounds.com/minecraft/aeadditions/commit/c3ee9ea64645eb6) DrummerMC *2015-11-30 20:35:23*


### Jira pr-191   

**Merge branch 'pr-191' into rv2**


[6af651c5771296f](https://git.the9grounds.com/minecraft/aeadditions/commit/6af651c5771296f) DrummerMC *2015-04-07 13:07:00*

**Merge branch 'pr-191' into rv2**


[94d96f6e3514bcc](https://git.the9grounds.com/minecraft/aeadditions/commit/94d96f6e3514bcc) DrummerMC *2015-04-07 12:33:07*


### No issue

**fixed gas terminal not selecting gas**


[3d6128e7f85c489](https://git.the9grounds.com/minecraft/aeadditions/commit/3d6128e7f85c489) DrummerMC *2017-11-08 16:53:09*

**fixed gas terminals not reset nextFilll**


[bf16c609c206792](https://git.the9grounds.com/minecraft/aeadditions/commit/bf16c609c206792) DrummerMC *2017-11-08 16:46:31*

**Update README.md**


[684a67cfae914e2](https://git.the9grounds.com/minecraft/aeadditions/commit/684a67cfae914e2) DrummerMC *2017-11-08 14:55:15*

**Update build.gradle**


[0cf4eb6489b8eef](https://git.the9grounds.com/minecraft/aeadditions/commit/0cf4eb6489b8eef) DrummerMC *2017-11-08 14:46:42*

**Update build.gradle**


[97eaf5abd18827c](https://git.the9grounds.com/minecraft/aeadditions/commit/97eaf5abd18827c) DrummerMC *2017-11-08 14:29:40*

**API Work**


[331f7757d23d075](https://git.the9grounds.com/minecraft/aeadditions/commit/331f7757d23d075) DrummerMC *2017-11-07 19:49:14*

**Fixed Fluid Filler GUI text**


[c9e74a59bfc01be](https://git.the9grounds.com/minecraft/aeadditions/commit/c9e74a59bfc01be) DrummerMC *2017-11-07 17:44:20*

**fixed getting incorect components on shift-click wiht storage cells**


[73a1c2c76dbc217](https://git.the9grounds.com/minecraft/aeadditions/commit/73a1c2c76dbc217) DrummerMC *2017-11-07 17:33:19*

**Update PowerItem.scala**


[85bfd1504c6a61b](https://git.the9grounds.com/minecraft/aeadditions/commit/85bfd1504c6a61b) DrummerMC *2017-11-06 18:57:34*

**Update zh_CN language file**


[e3b59e9aae169e0](https://git.the9grounds.com/minecraft/aeadditions/commit/e3b59e9aae169e0) TartaricAcid *2017-11-06 18:42:50*

**Update en_US.lang**

 * Renamed the Storage cells to follow AE2&#x27;s naming scheme as it gets kind of confusing and a little ocd trigger when you have &quot;64k Storage Cell&quot; and then also have &quot;256k Storage&quot;

[2c4940e553f109f](https://git.the9grounds.com/minecraft/aeadditions/commit/2c4940e553f109f) drunkripper *2017-11-06 18:42:31*

**Blast Resistent ME drive facing texture**


[bd8ba79eb109dad](https://git.the9grounds.com/minecraft/aeadditions/commit/bd8ba79eb109dad) DrummerMC *2017-11-05 18:49:29*

**EC 2.5.1**

 * - fixes crash on startup

[23c23e1fa9a8ff3](https://git.the9grounds.com/minecraft/aeadditions/commit/23c23e1fa9a8ff3) DrummerMC *2017-11-05 14:53:04*

**More 1.12 work**


[6cef0ac1159b36a](https://git.the9grounds.com/minecraft/aeadditions/commit/6cef0ac1159b36a) DrummerMC *2017-11-05 01:41:18*

**Update Recipes for 1.2**

 * Add Tooltips for Items in GUI&#x27;s
 * Fixed crash on server startup
 * Fixed WAILA Integration

[0a60bc89d554389](https://git.the9grounds.com/minecraft/aeadditions/commit/0a60bc89d554389) DrummerMC *2017-11-04 18:07:58*

**Update build.gradle**


[fc07e24d9801227](https://git.the9grounds.com/minecraft/aeadditions/commit/fc07e24d9801227) DrummerMC *2017-11-01 16:56:22*

**More 1.12 work**


[f491ede24d27eb1](https://git.the9grounds.com/minecraft/aeadditions/commit/f491ede24d27eb1) DrummerMC *2017-11-01 16:46:00*

**Work on 1.12**


[f5001676e8626a8](https://git.the9grounds.com/minecraft/aeadditions/commit/f5001676e8626a8) DrummerMC *2017-10-31 23:11:56*

**Update README.md**


[fa591db39717d35](https://git.the9grounds.com/minecraft/aeadditions/commit/fa591db39717d35) DrummerMC *2017-10-31 17:18:34*

**Fixed problems with fluid handling**


[e8895c26ef1b4ff](https://git.the9grounds.com/minecraft/aeadditions/commit/e8895c26ef1b4ff) DrummerMC *2017-10-31 16:53:34*

**Fixed Guis and gas storage cells**


[dbeccca251c00a0](https://git.the9grounds.com/minecraft/aeadditions/commit/dbeccca251c00a0) DrummerMC *2017-10-31 16:08:06*

**Update .travis.yml**


[a5aa7b888dce6c7](https://git.the9grounds.com/minecraft/aeadditions/commit/a5aa7b888dce6c7) DrummerMC *2017-10-31 14:48:08*

**Create .travis.yml**


[8170c3e9ec5e72e](https://git.the9grounds.com/minecraft/aeadditions/commit/8170c3e9ec5e72e) DrummerMC *2017-10-31 14:41:52*

**Walrus**


[b55d5d3f277686e](https://git.the9grounds.com/minecraft/aeadditions/commit/b55d5d3f277686e) DrummerMC *2017-10-31 13:58:21*

**Update AE and forge**


[4334265f725ff0d](https://git.the9grounds.com/minecraft/aeadditions/commit/4334265f725ff0d) DrummerMC *2017-10-31 09:35:01*

**Move Classes**


[7a01cd55c70ae00](https://git.the9grounds.com/minecraft/aeadditions/commit/7a01cd55c70ae00) Nedelosk *2017-10-10 13:59:42*

**Clean Up Inventories**


[14eaaf5f30679ae](https://git.the9grounds.com/minecraft/aeadditions/commit/14eaaf5f30679ae) Nedelosk *2017-10-10 12:34:41*

**Clean Up Gui Widgets**


[bf74adef5079571](https://git.the9grounds.com/minecraft/aeadditions/commit/bf74adef5079571) Nedelosk *2017-10-10 11:56:53*

**Add comments to the drive model files**


[5bf717a917ac500](https://git.the9grounds.com/minecraft/aeadditions/commit/5bf717a917ac500) Nedelosk *2017-10-09 22:46:06*

**Clean Up**


[132fddfdf245240](https://git.the9grounds.com/minecraft/aeadditions/commit/132fddfdf245240) Nedelosk *2017-10-09 19:47:33*

**Improve Fluid Level Emitter**


[c1809b01286cefe](https://git.the9grounds.com/minecraft/aeadditions/commit/c1809b01286cefe) Nedelosk *2017-10-09 15:49:12*

**Clean Up Guis**


[2bb51959b78d478](https://git.the9grounds.com/minecraft/aeadditions/commit/2bb51959b78d478) Nedelosk *2017-10-09 13:59:51*

**Clean Up**


[26018ceff6dcf96](https://git.the9grounds.com/minecraft/aeadditions/commit/26018ceff6dcf96) Nedelosk *2017-10-09 13:45:55*

**Clean Up Parts**


[1144eecc762f98a](https://git.the9grounds.com/minecraft/aeadditions/commit/1144eecc762f98a) Nedelosk *2017-10-09 13:32:31*

**Clean Up Fluid Interface**


[eae2c5acdd8e26d](https://git.the9grounds.com/minecraft/aeadditions/commit/eae2c5acdd8e26d) Nedelosk *2017-10-09 12:57:20*

**Clean Up Part Drive**


[930526521db2b7d](https://git.the9grounds.com/minecraft/aeadditions/commit/930526521db2b7d) Nedelosk *2017-10-09 11:08:42*

**Fix tooltip position**


[6f1d07383a46758](https://git.the9grounds.com/minecraft/aeadditions/commit/6f1d07383a46758) Nedelosk *2017-10-09 01:57:50*

**Fix Widget Tooltips**


[fa98a3a1c5eb58c](https://git.the9grounds.com/minecraft/aeadditions/commit/fa98a3a1c5eb58c) Nedelosk *2017-10-09 01:53:46*

**Added simple AND-Expression Parser for OreDictExporter**


[fdb324d04462708](https://git.the9grounds.com/minecraft/aeadditions/commit/fdb324d04462708) Nedelosk *2017-10-09 00:57:59*

**Fix Drive Fixture Model**


[4cd8103df9fc2e4](https://git.the9grounds.com/minecraft/aeadditions/commit/4cd8103df9fc2e4) Nedelosk *2017-10-09 00:20:11*

**Fix Hard Me Drive Model**


[3b1678306e18cf6](https://git.the9grounds.com/minecraft/aeadditions/commit/3b1678306e18cf6) Nedelosk *2017-10-08 22:05:39*

**Clean Up**


[0675bf2b34108ba](https://git.the9grounds.com/minecraft/aeadditions/commit/0675bf2b34108ba) Nedelosk *2017-09-05 14:25:37*

**Clean Up Parts**


[43d3968e0eac345](https://git.the9grounds.com/minecraft/aeadditions/commit/43d3968e0eac345) Nedelosk *2017-09-02 08:27:28*

**Clean Up Widgets**


[c84407519116473](https://git.the9grounds.com/minecraft/aeadditions/commit/c84407519116473) Nedelosk *2017-09-01 19:30:47*

**Clean Up Guis**


[15cdd5bc030ab54](https://git.the9grounds.com/minecraft/aeadditions/commit/15cdd5bc030ab54) Nedelosk *2017-09-01 14:42:10*

**Move Packet registration to Proxies**


[6f37e879605f2dc](https://git.the9grounds.com/minecraft/aeadditions/commit/6f37e879605f2dc) Nedelosk *2017-09-01 14:10:49*

**Finish Network Rework**


[ca4502bed1825ae](https://git.the9grounds.com/minecraft/aeadditions/commit/ca4502bed1825ae) Nedelosk *2017-09-01 14:09:32*

**Optimize Imports**


[e232ca3a76007eb](https://git.the9grounds.com/minecraft/aeadditions/commit/e232ca3a76007eb) Nedelosk *2017-08-27 13:37:11*

**Clean Up FluidIO and Emitter Packet**


[190584b4bc5f5b6](https://git.the9grounds.com/minecraft/aeadditions/commit/190584b4bc5f5b6) Nedelosk *2017-08-27 13:35:36*

**Fix Storage Container**


[fd634df57331202](https://git.the9grounds.com/minecraft/aeadditions/commit/fd634df57331202) Nedelosk *2017-08-26 20:59:52*

**Clean Up Fluid Helper**


[07ab2450e0a4efd](https://git.the9grounds.com/minecraft/aeadditions/commit/07ab2450e0a4efd) Nedelosk *2017-08-26 20:49:38*

**Clean Up Storage Gui and Container**


[ed1fb35b2d3496f](https://git.the9grounds.com/minecraft/aeadditions/commit/ed1fb35b2d3496f) Nedelosk *2017-08-26 18:25:11*

**Start Network Clean Up**


[f594ed0da2ac1a0](https://git.the9grounds.com/minecraft/aeadditions/commit/f594ed0da2ac1a0) Nedelosk *2017-08-26 14:24:09*

**Clean Up**


[2bc82a6020e203c](https://git.the9grounds.com/minecraft/aeadditions/commit/2bc82a6020e203c) Nedelosk *2017-08-25 23:25:20*

**Fix recipes and add fluid pattern model**


[27f65cbba1ff26b](https://git.the9grounds.com/minecraft/aeadditions/commit/27f65cbba1ff26b) Nedelosk *2017-08-25 22:38:35*

**Add almost all missing models**

 * Only missing:
 * -drives
 * -fluid pattern
 * -walrus

[8539dd9d19e2e2b](https://git.the9grounds.com/minecraft/aeadditions/commit/8539dd9d19e2e2b) Nedelosk *2017-08-25 21:27:47*

**Add more models**


[e3beb9591297345](https://git.the9grounds.com/minecraft/aeadditions/commit/e3beb9591297345) Nedelosk *2017-08-22 20:11:34*

**Fix some models**


[d2b5ac871d9c194](https://git.the9grounds.com/minecraft/aeadditions/commit/d2b5ac871d9c194) Nedelosk *2017-08-20 13:23:02*

**Remove Unused Dependencies**


[b480f999ca1dd38](https://git.the9grounds.com/minecraft/aeadditions/commit/b480f999ca1dd38) Nedelosk *2017-08-19 00:05:38*

**Update Dependencies**


[50d46c65028c4e2](https://git.the9grounds.com/minecraft/aeadditions/commit/50d46c65028c4e2) Nedelosk *2017-08-18 23:56:52*

**Optimize Imports**


[008dbae2527bb52](https://git.the9grounds.com/minecraft/aeadditions/commit/008dbae2527bb52) Nedelosk *2017-08-18 23:17:25*

**Fix all Errors**


[cd77ce930b44493](https://git.the9grounds.com/minecraft/aeadditions/commit/cd77ce930b44493) Nedelosk *2017-08-18 23:10:38*

**Clean Up**


[9d3fc247e0595f7](https://git.the9grounds.com/minecraft/aeadditions/commit/9d3fc247e0595f7) Nedelosk *2017-08-18 20:53:36*

**Clean Up**


[aa3616798bb2382](https://git.the9grounds.com/minecraft/aeadditions/commit/aa3616798bb2382) Nedelosk *2017-08-14 16:39:14*

**Work on Mod Integration**


[7f3b41bd6c43021](https://git.the9grounds.com/minecraft/aeadditions/commit/7f3b41bd6c43021) Nedelosk *2017-08-14 16:26:55*

**Work on Tile Entities**


[cdf5518cd7c054f](https://git.the9grounds.com/minecraft/aeadditions/commit/cdf5518cd7c054f) Nedelosk *2017-08-14 15:15:00*

**More 1.10.2 work**


[f6d5fb13f1e13da](https://git.the9grounds.com/minecraft/aeadditions/commit/f6d5fb13f1e13da) Nedelosk *2017-08-09 22:52:14*

**More 1.10.2 work**


[9c8e0f8d11b65e5](https://git.the9grounds.com/minecraft/aeadditions/commit/9c8e0f8d11b65e5) DrummerMC *2017-03-05 11:31:01*

**Work on 1.10.2**


[8338d2876768d1c](https://git.the9grounds.com/minecraft/aeadditions/commit/8338d2876768d1c) DrummerMC *2017-03-04 17:48:58*

**ExtraDução 2**

 * oy vey

[47394c09cd816cc](https://git.the9grounds.com/minecraft/aeadditions/commit/47394c09cd816cc) Lorenzo Dalla Costa Cervelin *2016-09-06 03:29:38*

**Update BlockCertusTank.java**

 * Full dupe fix. Sorry for my last commit.

[9e4cc71e097833d](https://git.the9grounds.com/minecraft/aeadditions/commit/9e4cc71e097833d) yaroslav4167 *2016-06-18 20:14:10*

**Fixed only swith between gas and essentia on universal terminal**


[92fac7e4382cd14](https://git.the9grounds.com/minecraft/aeadditions/commit/92fac7e4382cd14) DrummerMC *2016-06-14 15:38:19*

**EC 2.3.12**

 * - Added Name for tooltip
 * - Can&#x27;t open the WCT with the key without installed module

[70515b16239f1fc](https://git.the9grounds.com/minecraft/aeadditions/commit/70515b16239f1fc) DrummerMC *2016-06-14 15:11:38*

**Fixed crash with nei**


[8a86c2710a0b91e](https://git.the9grounds.com/minecraft/aeadditions/commit/8a86c2710a0b91e) DrummerMC *2016-06-13 20:00:42*

**Merge branch 'rv2' of github.com:ExtraCells/ExtraCells2 into rv2**


[771486dbbdb2765](https://git.the9grounds.com/minecraft/aeadditions/commit/771486dbbdb2765) DrummerMC *2016-06-13 16:52:13*

**EC 2.3.11**

 * - Add Universal Terminal Module: Crafting Terminal (require Wireless Crafting Terminal mod)

[83f9c0e61f03099](https://git.the9grounds.com/minecraft/aeadditions/commit/83f9c0e61f03099) DrummerMC *2016-06-13 16:52:01*

**Update BlockCertusTank.java**


[a634e5225d71d0c](https://git.the9grounds.com/minecraft/aeadditions/commit/a634e5225d71d0c) yaroslav4167 *2016-05-29 18:48:24*

**EC 2.3.10**


[e1d59469a755cae](https://git.the9grounds.com/minecraft/aeadditions/commit/e1d59469a755cae) DrummerMC *2016-05-16 15:37:46*

**Fix bedrock breaking in 3*3 mode**


[7165f4b7d99bfd2](https://git.the9grounds.com/minecraft/aeadditions/commit/7165f4b7d99bfd2) Livan *2016-03-05 14:17:14*

**EC 2.3.9**

 * - Update OpenComputers

[b66e378ed73ce1c](https://git.the9grounds.com/minecraft/aeadditions/commit/b66e378ed73ce1c) DrummerMC *2016-02-07 17:05:23*

**Move OpenComputers Upgrade to extracells.item**


[d6b45844ec3541f](https://git.the9grounds.com/minecraft/aeadditions/commit/d6b45844ec3541f) DrummerMC *2016-01-30 15:19:31*

**EC 2.3.8**


[620472d5770647e](https://git.the9grounds.com/minecraft/aeadditions/commit/620472d5770647e) DrummerMC *2016-01-29 16:44:38*

**Work on Gas Interface**


[6aa8d149534fb56](https://git.the9grounds.com/minecraft/aeadditions/commit/6aa8d149534fb56) DrummerMC *2016-12-31 15:26:37*

**Added recipe for Gas Storage Bus**


[9070df1039d84e7](https://git.the9grounds.com/minecraft/aeadditions/commit/9070df1039d84e7) DrummerMC *2016-12-29 18:37:46*

**More Bug fixes**


[445134f645a5231](https://git.the9grounds.com/minecraft/aeadditions/commit/445134f645a5231) DrummerMC *2016-12-29 16:28:36*

**Bux fixes**


[0766bf41a39d443](https://git.the9grounds.com/minecraft/aeadditions/commit/0766bf41a39d443) DrummerMC *2016-12-29 15:58:00*

**Gas Storage bus now should work with mekanism gas tank**


[9fd09d467673cc8](https://git.the9grounds.com/minecraft/aeadditions/commit/9fd09d467673cc8) DrummerMC *2016-12-29 15:38:45*

**Work on Gas Storage Bus**


[42d9a11906e883c](https://git.the9grounds.com/minecraft/aeadditions/commit/42d9a11906e883c) DrummerMC *2016-12-29 15:33:55*

**Merge branch 'rv2' of https://github.com/ExtraCells/ExtraCells2 into rv2**


[60b2e1d3f6ee302](https://git.the9grounds.com/minecraft/aeadditions/commit/60b2e1d3f6ee302) DrummerMC *2016-12-29 13:38:19*

**Japanese translation files**

 * Hello!!
 * As we have created a Japanese translation files , and you sure you want to got to add when it is good?

[80511efd806696c](https://git.the9grounds.com/minecraft/aeadditions/commit/80511efd806696c) Tier *2016-12-29 08:16:16*

**EC 2.3.7**


[346688f6dd4fc19](https://git.the9grounds.com/minecraft/aeadditions/commit/346688f6dd4fc19) DrummerMC *2016-12-28 12:52:50*

**Work on Fluid Storage Bus**


[6b535e0126bf8ee](https://git.the9grounds.com/minecraft/aeadditions/commit/6b535e0126bf8ee) DrummerMC *2015-12-26 13:06:45*

**Added Portable Gas Storage cell and fixed incorrect tooltip**


[3c55b97a3ad322f](https://git.the9grounds.com/minecraft/aeadditions/commit/3c55b97a3ad322f) DrummerMC *2015-12-25 16:40:36*

**EC 2.3.6**


[4c4ba0db618dff4](https://git.the9grounds.com/minecraft/aeadditions/commit/4c4ba0db618dff4) DrummerMC *2015-12-25 11:38:33*

**EC 2.3.4**


[82e9e27e0cb832e](https://git.the9grounds.com/minecraft/aeadditions/commit/82e9e27e0cb832e) DrummerMC *2015-12-17 21:07:35*

**Fixec compile crash**


[ca9ffc7f7fd41f0](https://git.the9grounds.com/minecraft/aeadditions/commit/ca9ffc7f7fd41f0) DrummerMC *2015-12-14 20:33:03*

**Remove unused Imports**


[4ac8d4c0f59ac46](https://git.the9grounds.com/minecraft/aeadditions/commit/4ac8d4c0f59ac46) DrummerMC *2015-12-14 20:21:15*

**Added NEI recipe for UniversalWirelesTerminal**


[ef8523a1d3f5e83](https://git.the9grounds.com/minecraft/aeadditions/commit/ef8523a1d3f5e83) DrummerMC *2015-12-14 17:53:38*

**Update zh_CN.lang**


[3c43e50dd85a2b7](https://git.the9grounds.com/minecraft/aeadditions/commit/3c43e50dd85a2b7) Urey.X *2015-12-10 01:19:56*

**Merge remote-tracking branch 'origin/rv2' into rv2**


[16003acf49791d8](https://git.the9grounds.com/minecraft/aeadditions/commit/16003acf49791d8) DrummerMC *2015-12-02 21:12:59*

**EC 2.3.3**

 * - Added OC Driver for Gas Import/Export Bus

[bd4fa79c4b5a155](https://git.the9grounds.com/minecraft/aeadditions/commit/bd4fa79c4b5a155) DrummerMC *2015-12-02 20:59:07*

**Update hu_HU.lang**

 * Fixed some derpy mistakes that didnt make sense.

[b68b8c02da94120](https://git.the9grounds.com/minecraft/aeadditions/commit/b68b8c02da94120) XFRGod *2015-11-30 18:56:38*

**Update hu_HU.lang**

 * Translated the gas stuff.

[8c7635daf0e39bf](https://git.the9grounds.com/minecraft/aeadditions/commit/8c7635daf0e39bf) XFRGod *2015-11-30 13:15:05*

**EC 2.3.2**


[0acfd66f0c06158](https://git.the9grounds.com/minecraft/aeadditions/commit/0acfd66f0c06158) DrummerMC *2015-11-29 19:57:23*

**Fixed Startup crash**


[cd2fb6d221185e2](https://git.the9grounds.com/minecraft/aeadditions/commit/cd2fb6d221185e2) DrummerMC *2015-11-29 19:54:31*

**Added textures**


[d32beddd5df7057](https://git.the9grounds.com/minecraft/aeadditions/commit/d32beddd5df7057) DrummerMC *2015-11-29 18:46:28*

**Added crafting recipes for gas devices**


[6f8f4d8b3307d49](https://git.the9grounds.com/minecraft/aeadditions/commit/6f8f4d8b3307d49) DrummerMC *2015-11-29 18:42:24*

**Merge remote-tracking branch 'origin/rv2' into rv2**


[548cdd1ba8b6791](https://git.the9grounds.com/minecraft/aeadditions/commit/548cdd1ba8b6791) DrummerMC *2015-11-29 15:14:06*

**Added universal wireless Terminal**


[28acb9a41b291ac](https://git.the9grounds.com/minecraft/aeadditions/commit/28acb9a41b291ac) DrummerMC *2015-11-29 15:13:46*

**OC Upgrade is now in OC Creative tab**


[33146cdfb39f3c6](https://git.the9grounds.com/minecraft/aeadditions/commit/33146cdfb39f3c6) DrummerMC *2015-11-29 11:31:59*

**Fixed lang file**


[e79469b0ac3a41e](https://git.the9grounds.com/minecraft/aeadditions/commit/e79469b0ac3a41e) DrummerMC *2015-11-29 11:09:15*

**Code clean up**


[525239c30e26022](https://git.the9grounds.com/minecraft/aeadditions/commit/525239c30e26022) DrummerMC *2015-11-29 10:59:42*

**Small Clean Up**


[27b72590d134655](https://git.the9grounds.com/minecraft/aeadditions/commit/27b72590d134655) Nedelosk *2015-11-28 23:52:02*

**Finished Gas System (Creative Only)**

 * #fix 305
 * #fix 290
 * #fix 266 maby
 * #fix 264

[7accd33461b623d](https://git.the9grounds.com/minecraft/aeadditions/commit/7accd33461b623d) DrummerMC *2015-11-28 17:36:48*

**Create hu_HU.lang**

 * I&#x27;d like to be the one maintaining the Hungarian translation and as such I made the current Hungarian language file.

[8b3c78a393adbf9](https://git.the9grounds.com/minecraft/aeadditions/commit/8b3c78a393adbf9) XFRGod *2015-11-28 17:10:01*

**Fix "Already tesselating!" error with the fluid storage monitor**


[bc59e3cbb7c86cb](https://git.the9grounds.com/minecraft/aeadditions/commit/bc59e3cbb7c86cb) Aesen Vismea *2015-11-08 03:51:15*

**Remove usage of deprecated AE2 API.**


[01cd2abe22dffea](https://git.the9grounds.com/minecraft/aeadditions/commit/01cd2abe22dffea) ruifung *2015-09-27 02:06:03*

**Update ItemStoragePortableCell.java**


[ba0c0e5f6490065](https://git.the9grounds.com/minecraft/aeadditions/commit/ba0c0e5f6490065) AmethystAir *2015-09-23 19:16:45*

**Fix item dupe introduced by making parts drop inventory contents.**


[f89eb9830cdfc37](https://git.the9grounds.com/minecraft/aeadditions/commit/f89eb9830cdfc37) ruifung *2015-09-11 03:16:18*

**Add checks so storage busses, IO busses and fluid terminals close when part is no longer valid.**


[cd9d308c2d1fbb5](https://git.the9grounds.com/minecraft/aeadditions/commit/cd9d308c2d1fbb5) ruifung *2015-09-10 03:44:43*

**Do not equal trade blocks with negative hardness.**


[0d38491ea9016e4](https://git.the9grounds.com/minecraft/aeadditions/commit/0d38491ea9016e4) Yip Rui Fung *2015-08-07 14:47:16*

**Added GasStorageCell, GasTerminal, Fixed Gas Import Bus**


[b08d43a101bafc9](https://git.the9grounds.com/minecraft/aeadditions/commit/b08d43a101bafc9) DrummerMC *2015-06-16 16:35:52*

**Fixed crash**


[fafd65c02122a25](https://git.the9grounds.com/minecraft/aeadditions/commit/fafd65c02122a25) DrummerMC *2015-06-14 17:21:47*

**Added sources to deobj jar**


[b4205d0c0efa484](https://git.the9grounds.com/minecraft/aeadditions/commit/b4205d0c0efa484) DrummerMC *2015-06-14 14:46:13*

**EC 2.2.73**


[6f0df8186d64ee2](https://git.the9grounds.com/minecraft/aeadditions/commit/6f0df8186d64ee2) DrummerMC *2015-06-14 14:01:08*

**Enable gas export bus**


[1e2828d187dbab5](https://git.the9grounds.com/minecraft/aeadditions/commit/1e2828d187dbab5) DrummerMC *2015-06-14 10:39:59*

**EC 2.2.72**

 * - bug fixes
 * - added ingame wiki mod support
 * - work on mekanism  gas support
 * - bug fixes

[f078dc75fb033d4](https://git.the9grounds.com/minecraft/aeadditions/commit/f078dc75fb033d4) DrummerMC *2015-06-13 15:41:49*

**Add the "es_ES.lang"**

 * We translation in Spanish so the people can read it.
 * Thank to my team of Mega Planet (www.megaplanet.net).

[7277afc2c3bf39e](https://git.the9grounds.com/minecraft/aeadditions/commit/7277afc2c3bf39e) Charles *2015-05-31 19:51:32*

**Merge branch 'rv2' of https://github.com/ExtraCells/ExtraCells2 into rv2**


[e09a2d3d5c63767](https://git.the9grounds.com/minecraft/aeadditions/commit/e09a2d3d5c63767) DrummerMC *2015-05-26 17:08:02*

**Open Computers upgrade now comsume power**


[55ee57e82501ef6](https://git.the9grounds.com/minecraft/aeadditions/commit/55ee57e82501ef6) DrummerMC *2015-05-26 17:07:55*

**Updated gradle to version 2.4**


[329a296f92bccb5](https://git.the9grounds.com/minecraft/aeadditions/commit/329a296f92bccb5) Halvor Lyche Strandvoll *2015-05-26 12:45:29*

**Added Open Computers ingame manual support**


[4c39a82c61204fd](https://git.the9grounds.com/minecraft/aeadditions/commit/4c39a82c61204fd) DrummerMC *2015-05-26 11:34:57*

**Added functions to OC Upgrade**

 * OC Upgrade now works in drones

[1476808b15367c2](https://git.the9grounds.com/minecraft/aeadditions/commit/1476808b15367c2) DrummerMC *2015-05-26 01:10:32*

**Merge branch 'rv2' of https://github.com/ExtraCells/ExtraCells2 into rv2**


[12bdf5ed261da25](https://git.the9grounds.com/minecraft/aeadditions/commit/12bdf5ed261da25) DrummerMC *2015-05-25 17:55:45*

**Added OpenComputers ME Upgrade**


[cabc6716091ec0e](https://git.the9grounds.com/minecraft/aeadditions/commit/cabc6716091ec0e) DrummerMC *2015-05-25 17:55:36*

**Update zh_TW.lang**


[d3cd1baf8f0da3b](https://git.the9grounds.com/minecraft/aeadditions/commit/d3cd1baf8f0da3b) Shen-Ta Hsieh *2015-05-10 05:40:26*

**Update zh_CN.lang**


[2a0e483f9966719](https://git.the9grounds.com/minecraft/aeadditions/commit/2a0e483f9966719) bakaxyf *2015-04-20 02:37:46*

**Update ru_RU.lang**


[41ab95bad22e5b4](https://git.the9grounds.com/minecraft/aeadditions/commit/41ab95bad22e5b4) Anton *2015-04-18 16:47:39*

**EC 2.2.69**


[2eaf2c1fbecb762](https://git.the9grounds.com/minecraft/aeadditions/commit/2eaf2c1fbecb762) DrummerMC *2015-04-12 10:54:48*

**Fluid Vibration Chamber no longer needs a channel**


[e0f3ce9e745e01a](https://git.the9grounds.com/minecraft/aeadditions/commit/e0f3ce9e745e01a) DrummerMC *2015-04-12 10:54:28*

**Fixed Fluid Vibrant Chamber not connect to ae network**


[15674cd73cc4288](https://git.the9grounds.com/minecraft/aeadditions/commit/15674cd73cc4288) DrummerMC *2015-04-11 23:10:25*

**Work on FluidVibrationChamber**


[64ed1cb753982f8](https://git.the9grounds.com/minecraft/aeadditions/commit/64ed1cb753982f8) DrummerMC *2015-04-11 16:18:59*

**EC 2.2.68**


[67cb4084064de81](https://git.the9grounds.com/minecraft/aeadditions/commit/67cb4084064de81) DrummerMC *2015-04-11 12:22:20*

**Update ae**


[5f3ab603b42e9b8](https://git.the9grounds.com/minecraft/aeadditions/commit/5f3ab603b42e9b8) DrummerMC *2015-04-11 11:56:51*

**Added NEI Integration**


[2235ca2b7e7c0fb](https://git.the9grounds.com/minecraft/aeadditions/commit/2235ca2b7e7c0fb) DrummerMC *2015-04-11 11:56:28*

**Fixed Open Computers Integration**


[913ca6158934c4b](https://git.the9grounds.com/minecraft/aeadditions/commit/913ca6158934c4b) DrummerMC *2015-04-11 11:55:58*

**update to MIT license**


[543aa7fc69d596f](https://git.the9grounds.com/minecraft/aeadditions/commit/543aa7fc69d596f) Leon Loeser *2015-04-10 20:58:36*

**Update zh_CN.lang**


[e819fae167b9bb9](https://git.the9grounds.com/minecraft/aeadditions/commit/e819fae167b9bb9) bakaxyf *2015-04-10 06:42:14*

**EC 2.2.67**


[9d654b156e70cd0](https://git.the9grounds.com/minecraft/aeadditions/commit/9d654b156e70cd0) DrummerMC *2015-04-08 17:28:20*

**Added name for blast resistand me drive**


[2df606b9c2da3c5](https://git.the9grounds.com/minecraft/aeadditions/commit/2df606b9c2da3c5) DrummerMC *2015-04-08 17:25:48*

**Added Crafting Recipe for Fluid Vibran Chamber**


[fb23de536e2e509](https://git.the9grounds.com/minecraft/aeadditions/commit/fb23de536e2e509) Alheimerjung *2015-04-07 21:11:27*

**EC 2.2.66**


[966891c3ec6c09d](https://git.the9grounds.com/minecraft/aeadditions/commit/966891c3ec6c09d) DrummerMC *2015-04-07 18:08:45*

**Added Crafting Recipes name for VibranChamber**


[7cb2842c66be653](https://git.the9grounds.com/minecraft/aeadditions/commit/7cb2842c66be653) DrummerMC *2015-04-07 16:37:42*

**Work on VibrationChamber/HardMEDrive**


[399cd601e5c255c](https://git.the9grounds.com/minecraft/aeadditions/commit/399cd601e5c255c) DrummerMC *2015-04-07 15:31:57*

**Energy Fix**


[9441af45674e92e](https://git.the9grounds.com/minecraft/aeadditions/commit/9441af45674e92e) Alheimerjung *2015-04-07 13:05:45*

**Fix**


[1aaa3faa5ba5c5a](https://git.the9grounds.com/minecraft/aeadditions/commit/1aaa3faa5ba5c5a) Alheimerjung *2015-04-07 11:42:51*

**Fluid Vibration Chamber Beta**


[6100c5284cc877b](https://git.the9grounds.com/minecraft/aeadditions/commit/6100c5284cc877b) Alheimerjung *2015-04-07 11:39:41*

**Update zh_CN.lang**


[01146fd263b652f](https://git.the9grounds.com/minecraft/aeadditions/commit/01146fd263b652f) bakaxyf *2015-04-07 07:01:16*

**More Work on BlockHardMEDrive**


[6a23d45e5012347](https://git.the9grounds.com/minecraft/aeadditions/commit/6a23d45e5012347) DrummerMC *2015-04-06 20:42:33*

**Update build.gradle to work with idea**


[67a357df73346b8](https://git.the9grounds.com/minecraft/aeadditions/commit/67a357df73346b8) DrummerMC *2015-04-06 20:03:02*

**Work on blast resistend me drive**


[a58d5e5c747d968](https://git.the9grounds.com/minecraft/aeadditions/commit/a58d5e5c747d968) DrummerMC *2015-04-06 12:55:39*

**Update build.gradle**


[84c39b0249e0f0a](https://git.the9grounds.com/minecraft/aeadditions/commit/84c39b0249e0f0a) DrummerMC *2015-04-06 00:26:13*

**Fixed Compile crash**


[c55fca778562482](https://git.the9grounds.com/minecraft/aeadditions/commit/c55fca778562482) DrummerMC *2015-04-05 21:32:23*

**Changed Some code to scala**


[bd1f64e62bc57ee](https://git.the9grounds.com/minecraft/aeadditions/commit/bd1f64e62bc57ee) DrummerMC *2015-04-05 20:51:33*

**Additional integration can now be disabled in the config**


[662971365586712](https://git.the9grounds.com/minecraft/aeadditions/commit/662971365586712) DrummerMC *2015-04-05 14:32:21*

**Better Open Computers Support**


[f01199c30e99f3b](https://git.the9grounds.com/minecraft/aeadditions/commit/f01199c30e99f3b) DrummerMC *2015-04-05 14:30:51*

**Fixed build.gradle**


[ec30b253c5b1c8a](https://git.the9grounds.com/minecraft/aeadditions/commit/ec30b253c5b1c8a) DrummerMC *2015-04-02 17:47:53*

**EC 2.2.64**

 * - Update forge
 * - Update dependencies
 * - Added Open Computers support

[f716dd589d0eb5e](https://git.the9grounds.com/minecraft/aeadditions/commit/f716dd589d0eb5e) DrummerMC *2015-04-02 16:03:09*

**EC 2.2.63**

 * - fixed crash with fluid storage bus

[ca8f25baa40745b](https://git.the9grounds.com/minecraft/aeadditions/commit/ca8f25baa40745b) DrummerMC *2015-03-29 12:06:44*

**EC 2.2.62**

 * - fixed performance problem on FluidStorageBus

[147ab6c3f4fa890](https://git.the9grounds.com/minecraft/aeadditions/commit/147ab6c3f4fa890) DrummerMC *2015-03-25 16:55:01*

**Code Style**


[45fbd0166329bee](https://git.the9grounds.com/minecraft/aeadditions/commit/45fbd0166329bee) DrummerMC *2015-03-23 17:40:44*

**Code Style**


[d5c622096f6a820](https://git.the9grounds.com/minecraft/aeadditions/commit/d5c622096f6a820) DrummerMC *2015-03-23 15:22:52*

**EC 2.2.60**

 * - work on fluid interface gui

[458e4c661fcf1d0](https://git.the9grounds.com/minecraft/aeadditions/commit/458e4c661fcf1d0) DrummerMC *2015-03-22 21:15:02*

**EC 2.2.59**

 * - Fixed Item removing in fluid interface
 * - shift + left mouse click now work on  the interface terminal
 * - fixed other bugs on interface terminal

[c2046c7d74c276f](https://git.the9grounds.com/minecraft/aeadditions/commit/c2046c7d74c276f) DrummerMC *2015-03-15 21:25:19*

**EC 2.2.58**

 * - fixed item delieting with fluid crafting
 * - render work

[550098a6276520b](https://git.the9grounds.com/minecraft/aeadditions/commit/550098a6276520b) DrummerMC *2015-03-07 21:45:26*

**EC 2.2.57**

 * - fix # 126
 * - fix ore dictionary export bus crash
 * - added slot background items
 * - I/O configuration on fluid storage bus

[7bd9e67e0b83b59](https://git.the9grounds.com/minecraft/aeadditions/commit/7bd9e67e0b83b59) DrummerMC *2015-03-07 00:44:09*

**Fixed fluid storage bus ignore filter**


[530d33b0cb0df11](https://git.the9grounds.com/minecraft/aeadditions/commit/530d33b0cb0df11) DrummerMC *2015-03-03 18:31:13*

**Edit versions number**


[b9934f21c4e835d](https://git.the9grounds.com/minecraft/aeadditions/commit/b9934f21c4e835d) DrummerMC *2015-02-16 09:31:46*

**Update fr_FR.lang**


[0f8267a25b1972d](https://git.the9grounds.com/minecraft/aeadditions/commit/0f8267a25b1972d) DrummerMC *2015-02-05 15:48:44*

**Update build.properties**


[99a7c6865f0c283](https://git.the9grounds.com/minecraft/aeadditions/commit/99a7c6865f0c283) DrummerMC *2015-02-05 15:46:41*


## 2.2.52
### No issue

**EC 2.2.52**

 * - Bug fixes

[15cabd129f40a22](https://git.the9grounds.com/minecraft/aeadditions/commit/15cabd129f40a22) DrummerMC *2015-02-03 14:29:36*


## 2.2.5X
### No issue

**EC 2.2.51**

 * - Update ae
 * - Fixed fluid interface crafting

[14d69ac5c383dc1](https://git.the9grounds.com/minecraft/aeadditions/commit/14d69ac5c383dc1) DrummerMC *2015-02-02 18:11:06*


## 2.2.50
### GitLab [1](https://git.the9grounds.com/minecraft/aeadditions.git/issues/1) Allow Storage Cell sizes to be set via config    *Enhancement*  

**Merge pull request #1 from DrummerMC/rv1**

 * Rv1

[d2138ef8868d8de](https://git.the9grounds.com/minecraft/aeadditions/commit/d2138ef8868d8de) DrummerMC *2014-11-11 20:42:36*

**Merge pull request #1 from PaleoCrafter/master**

 * Cleaned up the project and changed some stuff

[91d5832c1a27a9e](https://git.the9grounds.com/minecraft/aeadditions/commit/91d5832c1a27a9e) Pwnie2012 *2013-06-10 19:34:39*


### GitLab 108   

**Merge pull request #108 from DrummerMC/rv2**

 * Added Fluid Crafter on rv2, Api Work and Added Receips

[5340b9abc3c260f](https://git.the9grounds.com/minecraft/aeadditions/commit/5340b9abc3c260f) Leon Loeser *2014-11-29 18:36:38*


### GitLab [11](https://git.the9grounds.com/minecraft/aeadditions.git/issues/11) Redo AbstractExtraCellsInventory to allow configurable number of max slots    *Enhancement*  

**Merge pull request #11 from Adaptivity/patch-1**

 * Create ru_RU.lang

[9f6985238b181b9](https://git.the9grounds.com/minecraft/aeadditions/commit/9f6985238b181b9) Leon Loeser *2014-05-18 08:13:15*


### GitLab 110   

**Fix #110**


[915172aae6c69b5](https://git.the9grounds.com/minecraft/aeadditions/commit/915172aae6c69b5) Yip Rui Fung *2014-12-09 05:04:37*


### GitLab 111   

**Merge pull request #111 from DrummerMC/rv2**

 * Fixed Fluid Crafter(rv2)

[2d8229bd01b012c](https://git.the9grounds.com/minecraft/aeadditions/commit/2d8229bd01b012c) Leon Loeser *2014-11-30 11:06:59*


### GitLab 112   

**Merge pull request #112 from delta534/master**

 * This should fix the server/client sync issues for the Certus Tanks.

[343d61165644da7](https://git.the9grounds.com/minecraft/aeadditions/commit/343d61165644da7) M3gaFr3ak *2014-01-14 08:21:44*


### GitLab 113   

**Merge pull request #113 from delta534/master**

 * Another Certus Tank Update, this time client rendering.

[3c7fb3a1e0c760a](https://git.the9grounds.com/minecraft/aeadditions/commit/3c7fb3a1e0c760a) M3gaFr3ak *2014-01-17 14:39:37*


### GitLab 115   

**Merge pull request #115 from DrummerMC/rv2**

 * Fixed #37, added Fluid Crafting ChamberReceipe, Wireless Term Api change,wireless term now use energy, portable cell and changed crafting chamber name(rv2)

[d9d951f4750ce3b](https://git.the9grounds.com/minecraft/aeadditions/commit/d9d951f4750ce3b) DrummerMC *2014-12-08 17:54:22*


### GitLab 124   

**Fixed #124 Fixed Cell Workbench copy mode**


[6c1439f05bb7152](https://git.the9grounds.com/minecraft/aeadditions/commit/6c1439f05bb7152) DrummerMC *2014-12-08 17:50:01*


### GitLab 129   

**EC 2.2.17**

 * Fixed #129

[786fba674aae8ce](https://git.the9grounds.com/minecraft/aeadditions/commit/786fba674aae8ce) DrummerMC *2014-12-16 19:08:51*

**Fixed #129**


[533415784c6369d](https://git.the9grounds.com/minecraft/aeadditions/commit/533415784c6369d) DrummerMC *2014-12-09 13:39:02*


### GitLab [13](https://git.the9grounds.com/minecraft/aeadditions.git/issues/13) Reimplement ItemOCUpgrade    *Enhancement*  *OpenComputers*  

**Merge pull request #13 from Vexatos/patch-4**

 * Updated de_DE.xml

[49151cf0b774239](https://git.the9grounds.com/minecraft/aeadditions/commit/49151cf0b774239) M3gaFr3ak *2013-08-18 20:28:26*


### GitLab 132   

**Fixed #132**


[28c12d1e879b9a8](https://git.the9grounds.com/minecraft/aeadditions/commit/28c12d1e879b9a8) DrummerMC *2014-12-11 19:49:28*


### GitLab 142   

**EC 2.2.21**

 * -Fixed #142
 * -work on Fluid Storage Bus

[0d02b833379e019](https://git.the9grounds.com/minecraft/aeadditions/commit/0d02b833379e019) DrummerMC *2014-12-21 11:24:57*


### GitLab 144   

**EC 2.2.23**

 * Fixed #144

[4d332ace84f003a](https://git.the9grounds.com/minecraft/aeadditions/commit/4d332ace84f003a) DrummerMC *2014-12-26 15:12:48*


### GitLab 149   

**EC 2.2.24**

 * Added #149

[3c0eab838e648a1](https://git.the9grounds.com/minecraft/aeadditions/commit/3c0eab838e648a1) DrummerMC *2015-01-04 14:00:12*


### GitLab 150   

**EC 22.2.25**

 * Fix #150, #153

[d5b130663f56e7d](https://git.the9grounds.com/minecraft/aeadditions/commit/d5b130663f56e7d) DrummerMC *2015-01-11 20:02:42*


### GitLab 153   

**EC 22.2.25**

 * Fix #150, #153

[d5b130663f56e7d](https://git.the9grounds.com/minecraft/aeadditions/commit/d5b130663f56e7d) DrummerMC *2015-01-11 20:02:42*


### GitLab 158   

**Merge pull request #158 from Alheimerjung/rv2**

 * Texturen for DrummerMC

[8f833942c913fbc](https://git.the9grounds.com/minecraft/aeadditions/commit/8f833942c913fbc) DrummerMC *2015-01-16 23:44:46*


### GitLab 17   

**Merge pull request #17 from dmodoomsirius/master**

 * update build.gradle to work with jenkins

[8f896dbc30a8da0](https://git.the9grounds.com/minecraft/aeadditions/commit/8f896dbc30a8da0) Leon Loeser *2014-05-24 21:04:48*


### GitLab 18   

**Merge pull request #18 from VeryBigBro/patch-1**

 * Russian localization

[c93949d7257adc4](https://git.the9grounds.com/minecraft/aeadditions/commit/c93949d7257adc4) M3gaFr3ak *2013-08-28 16:30:39*


### GitLab [2](https://git.the9grounds.com/minecraft/aeadditions.git/issues/2) Rewrite filter handling in export &amp; import bus    *Enhancement*  

**Merge pull request #2 from M3gaFr3ak/rv2**

 * Rv2

[d6cabcbe913481a](https://git.the9grounds.com/minecraft/aeadditions/commit/d6cabcbe913481a) DrummerMC *2014-11-15 08:55:15*

**Merge pull request #2 from Vexatos/patch-1**

 * Update de_DE.lang

[fe686b4e79565fc](https://git.the9grounds.com/minecraft/aeadditions/commit/fe686b4e79565fc) M3gaFr3ak *2014-03-27 20:07:29*

**Merge pull request #2 from PaleoCrafter/master**

 * Updated the textures

[bd74fbc95f9d1c8](https://git.the9grounds.com/minecraft/aeadditions/commit/bd74fbc95f9d1c8) Pwnie2012 *2013-06-11 14:47:28*


### GitLab 20   

**Merge pull request #20 from AlgorithmX2/patch-1**

 * Use the security api correctly.

[14b8a191923cffa](https://git.the9grounds.com/minecraft/aeadditions/commit/14b8a191923cffa) Leon Loeser *2014-05-25 10:15:34*


### GitLab 23   

**Merge pull request #23 from SinusoidalC/patch-1**

 * Create zh_CN.lang

[f3e24e03974db12](https://git.the9grounds.com/minecraft/aeadditions/commit/f3e24e03974db12) Leon Loeser *2014-05-26 07:46:57*

**Merge pull request #23 from Cisien/patch-1**

 * removed a redundant call to grid.getCellArray()

[4bb2d0079f5a53e](https://git.the9grounds.com/minecraft/aeadditions/commit/4bb2d0079f5a53e) M3gaFr3ak *2013-09-02 08:19:16*


### GitLab 26   

**Merge pull request #26 from crafteverywhere/patch-2**

 * Update zh_CN.xml

[f41f52409de5b19](https://git.the9grounds.com/minecraft/aeadditions/commit/f41f52409de5b19) M3gaFr3ak *2013-09-02 18:53:24*


### GitLab 27   

**Merge pull request #27 from crafteverywhere/patch-2**

 * The name of Chinese language file should be zh_CN

[19950f0a1a41db6](https://git.the9grounds.com/minecraft/aeadditions/commit/19950f0a1a41db6) M3gaFr3ak *2013-09-02 19:06:33*


### GitLab 28   

**Fix fluid formation plane, implement network ticking. Here you go, #28 :)**


[dc3ec462b8c0bdf](https://git.the9grounds.com/minecraft/aeadditions/commit/dc3ec462b8c0bdf) Wuestengecko *2014-08-19 11:46:04*


### GitLab [3](https://git.the9grounds.com/minecraft/aeadditions.git/issues/3) Fix duplication bug with gases    *Bug*  

**Merge pull request #3 from Vexatos/patch-2**

 * Update de_DE.lang

[476d8a0f706f599](https://git.the9grounds.com/minecraft/aeadditions/commit/476d8a0f706f599) M3gaFr3ak *2014-04-14 09:21:19*


### GitLab 31   

**Merge pull request #31 from Vexatos/patch-5**

 * Update de_DE.xml

[fdd41d6e53130de](https://git.the9grounds.com/minecraft/aeadditions/commit/fdd41d6e53130de) M3gaFr3ak *2013-09-04 15:57:13*


### GitLab 35   

**Merge pull request #35 from VeryBigBro/patch-3**

 * Update ru_RU.xml

[b1b5c74908d1edb](https://git.the9grounds.com/minecraft/aeadditions/commit/b1b5c74908d1edb) M3gaFr3ak *2013-09-24 15:35:52*


### GitLab 37   

**Merge pull request #115 from DrummerMC/rv2**

 * Fixed #37, added Fluid Crafting ChamberReceipe, Wireless Term Api change,wireless term now use energy, portable cell and changed crafting chamber name(rv2)

[d9d951f4750ce3b](https://git.the9grounds.com/minecraft/aeadditions/commit/d9d951f4750ce3b) DrummerMC *2014-12-08 17:54:22*

**Fixed #37 (rv2)**


[eacc2ac794a705a](https://git.the9grounds.com/minecraft/aeadditions/commit/eacc2ac794a705a) DrummerMC *2014-12-01 20:02:27*


### GitLab 38   

**Fluid Level Emitter: Update redstone when wanted amount was changed in GUI. Closes issue #38**


[33b35678ffa4890](https://git.the9grounds.com/minecraft/aeadditions/commit/33b35678ffa4890) Wuestengecko *2014-07-09 04:53:21*


### GitLab [4](https://git.the9grounds.com/minecraft/aeadditions.git/issues/4) Find a way to override BasicCellInventory    *Bug*  

**Merge pull request #4 from M3gaFr3ak/rv2**

 * Rv2

[13f5e1ffe43fdb6](https://git.the9grounds.com/minecraft/aeadditions/commit/13f5e1ffe43fdb6) DrummerMC *2014-11-15 23:27:44*


### GitLab 40   

**Merge pull request #40 from Vexatos/patch-1**

 * Update de_DE.xml

[d2ef3e56216e56e](https://git.the9grounds.com/minecraft/aeadditions/commit/d2ef3e56216e56e) M3gaFr3ak *2013-09-23 19:56:01*


### GitLab [5](https://git.the9grounds.com/minecraft/aeadditions.git/issues/5) Add ability to add acceleration cards to fluid auto crafter &amp; fluid auto filler    *Enhancement*  

**Merge pull request #5 from Vexatos/patch-1**

 * Create de_DE.xml

[592eae0b6d5b304](https://git.the9grounds.com/minecraft/aeadditions/commit/592eae0b6d5b304) M3gaFr3ak *2013-08-03 07:33:15*


### GitLab 50   

**Merge pull request #50 from Vexatos/patch-1**

 * Added a ton of hyphens (It is German, remember)

[09bd55ec0e4e8a3](https://git.the9grounds.com/minecraft/aeadditions/commit/09bd55ec0e4e8a3) M3gaFr3ak *2013-10-22 16:12:07*


### GitLab 54   

**Merge pull request #54 from crafteverywhere/patch-2**

 * Update zh_CN.xml

[4da0eff5db844b4](https://git.the9grounds.com/minecraft/aeadditions/commit/4da0eff5db844b4) M3gaFr3ak *2013-11-03 20:01:50*


### GitLab 58   

**Merge pull request #58 from Vexatos/patch-1**

 * Update de_DE.xml

[53f03376b115223](https://git.the9grounds.com/minecraft/aeadditions/commit/53f03376b115223) M3gaFr3ak *2013-11-03 20:02:01*


### GitLab 59   

**Merge pull request #59 from crafteverywhere/patch-2**

 * updated zh_CN.xml for ExtraCells 1.5.1

[85f2409b2fed57f](https://git.the9grounds.com/minecraft/aeadditions/commit/85f2409b2fed57f) M3gaFr3ak *2013-11-05 16:01:48*


### GitLab [6](https://git.the9grounds.com/minecraft/aeadditions.git/issues/6) New Features  

**Merge pull request #6 from M3gaFr3ak/rv2**

 * Rv2

[5beaf2513791c88](https://git.the9grounds.com/minecraft/aeadditions/commit/5beaf2513791c88) DrummerMC *2014-12-01 20:00:49*

**Merge pull request #6 from Vexatos/patch-2**

 * Update de_DE.xml

[6742a38c069591a](https://git.the9grounds.com/minecraft/aeadditions/commit/6742a38c069591a) M3gaFr3ak *2013-08-10 20:45:16*


### GitLab 62   

**EC 2.2.14**

 * Fixed #62

[682ed3691de72a7](https://git.the9grounds.com/minecraft/aeadditions/commit/682ed3691de72a7) DrummerMC *2014-12-13 20:55:31*


### GitLab 64   

**Merge pull request #64 from Vexatos/patch-1**

 * Update de_DE.xml

[f2f3a93f4e9ee3f](https://git.the9grounds.com/minecraft/aeadditions/commit/f2f3a93f4e9ee3f) M3gaFr3ak *2013-11-11 10:35:16*


### GitLab 66   

**Merge pull request #66 from sb023612/rv1**

 * Updated Chinese Localization

[52f9944595b0aa7](https://git.the9grounds.com/minecraft/aeadditions/commit/52f9944595b0aa7) Leon Loeser *2014-09-18 09:58:49*

**Merge pull request #66 from Vexatos/patch-1**

 * -updated german localization

[5244ded5d6a3236](https://git.the9grounds.com/minecraft/aeadditions/commit/5244ded5d6a3236) M3gaFr3ak *2013-11-13 19:01:15*


### GitLab 67   

**Merge pull request #67 from dmodoomsirius/rv1**

 * Include build number in filename

[14650ba58f6d7b2](https://git.the9grounds.com/minecraft/aeadditions/commit/14650ba58f6d7b2) Leon Loeser *2014-09-18 13:08:56*


### GitLab [7](https://git.the9grounds.com/minecraft/aeadditions.git/issues/7) Gas level emitter causes fps lag    *Bug*  

**Merge pull request #7 from M3gaFr3ak/rv2**

 * Rv2

[17cf6b526fcf863](https://git.the9grounds.com/minecraft/aeadditions/commit/17cf6b526fcf863) DrummerMC *2014-12-07 12:05:15*

**Merge pull request #7 from Vexatos/patch-3**

 * Why you no hyphens D:

[00eb5d94b0c4ab5](https://git.the9grounds.com/minecraft/aeadditions/commit/00eb5d94b0c4ab5) M3gaFr3ak *2014-05-04 17:23:11*

**Merge pull request #7 from Vexatos/patch-1**

 * Update de_DE.xml

[5192790cbc62ee2](https://git.the9grounds.com/minecraft/aeadditions/commit/5192790cbc62ee2) M3gaFr3ak *2013-08-11 16:38:28*


### GitLab 74   

**Better fix for #74, fixes #74.**

 * Also undoes my half assed fix in GUI code, this one is actually done server side as it should have been.

[f6dc4b5a8be243e](https://git.the9grounds.com/minecraft/aeadditions/commit/f6dc4b5a8be243e) Yip Rui Fung *2014-12-09 12:10:15*

**Bunch of fixes for ME Fluid Level Emitter GUI. Fixes #74**

 * - Fix unlocalized name in ME Fluid Level Emitter GUI
 * - Fix text field in ME Fluid Level Emitter not updating when buttons are pressed.
 * - Prevented buttons from setting negative values (Because what is the point of a negative value in the fluid emitter anyway?)
 * - Added missing call to draw redstone mode button tooltip - although its position is very off for some reason.

[ee7ced842316b2b](https://git.the9grounds.com/minecraft/aeadditions/commit/ee7ced842316b2b) Yip Rui Fung *2014-12-09 11:43:40*


### GitLab 75   

**Merge pull request #75 from arition/patch-1**

 * Fix ME_Fluid_Import_Bus&amp;ME_Fluid_Export_Bus names

[f1544b76b2f7206](https://git.the9grounds.com/minecraft/aeadditions/commit/f1544b76b2f7206) Leon Loeser *2014-09-28 13:47:46*


### GitLab 76   

**Merge pull request #76 from Adaptivity/patch-1**

 * Update ru_RU.lang

[4b35cb8c6184723](https://git.the9grounds.com/minecraft/aeadditions/commit/4b35cb8c6184723) Leon Loeser *2014-09-28 13:45:57*


### GitLab [8](https://git.the9grounds.com/minecraft/aeadditions.git/issues/8) Don&#x27;t turn on gas level emitter if there is no fluid selected    *Won&#x27;t Do*  

**Merge pull request #8 from Vexatos/patch-1**

 * Update de_DE.xml

[7155db7031b3e0f](https://git.the9grounds.com/minecraft/aeadditions/commit/7155db7031b3e0f) M3gaFr3ak *2013-08-11 17:17:45*


### GitLab 80   

**Merge pull request #80 from utoxin/master**

 * Fixing bug with storingPower

[b5ce503c6b365a6](https://git.the9grounds.com/minecraft/aeadditions/commit/b5ce503c6b365a6) M3gaFr3ak *2013-11-28 12:14:28*


### GitLab 83   

**Merge pull request #83 from Mazdallier/patch-2**

 * Create fr_FR.lang

[e0d7866bb362e3d](https://git.the9grounds.com/minecraft/aeadditions/commit/e0d7866bb362e3d) Leon Loeser *2014-10-14 15:14:53*


### GitLab 84   

**Merge pull request #84 from sb023612/rv2**

 * Fixed a few mistakes in zh_CN.lang

[19d01ce60407112](https://git.the9grounds.com/minecraft/aeadditions/commit/19d01ce60407112) Leon Loeser *2014-10-16 17:55:40*


### GitLab 88   

**Merge pull request #88 from Pyeroh/rv2**

 * Corrections on fr_FR.lang

[7bed9567cbbe75e](https://git.the9grounds.com/minecraft/aeadditions/commit/7bed9567cbbe75e) Leon Loeser *2014-10-20 13:55:10*


### GitLab [9](https://git.the9grounds.com/minecraft/aeadditions.git/issues/9) Export and Import Buses do not show up correctly when hovering over ae2 cards    *Bug*  

**Merge pull request #9 from crafteverywhere/patch-1**

 * Create zh_CN.xml

[da34724bec235fa](https://git.the9grounds.com/minecraft/aeadditions/commit/da34724bec235fa) M3gaFr3ak *2013-08-12 18:23:52*


### GitLab 91   

**Merge pull request #91 from Vexatos/patch-1**

 * Leere. Puh.

[8dd21b72ae0855e](https://git.the9grounds.com/minecraft/aeadditions/commit/8dd21b72ae0855e) M3gaFr3ak *2013-12-09 17:39:56*


### GitLab 93   

**Merge pull request #93 from marcin212/rv1**

 * Fixed partially duplicating fluid.

[011911b23e96bab](https://git.the9grounds.com/minecraft/aeadditions/commit/011911b23e96bab) Leon Loeser *2014-10-31 18:36:07*


### GitLab 94   

**Merge pull request #94 from marcin212/rv1**

 * Fixed cloning as simulations were not taken into account.

[0e084a1609a7c80](https://git.the9grounds.com/minecraft/aeadditions/commit/0e084a1609a7c80) Leon Loeser *2014-10-31 20:02:43*

**Merge pull request #94 from SSCXM/patch-2**

 * Update zh_CN.xml

[3970f1adb987c9e](https://git.the9grounds.com/minecraft/aeadditions/commit/3970f1adb987c9e) M3gaFr3ak *2013-12-10 16:33:18*


### GitLab 98   

**Merge pull request #98 from marcin212/rv1**

 * Fixed duplicating fluid.

[866674e0e7b96e1](https://git.the9grounds.com/minecraft/aeadditions/commit/866674e0e7b96e1) Leon Loeser *2014-11-02 14:48:11*


### Jira ISO-8859   

**Create de_DE.xml**

 * I made this translation fit to the translation of AE.
 * Any suggestions are appreciated!
 * I hope, this has to be be UTF-8 encoded, otherwise I will change it (If it is NOT UTF-8 but ISO-8859-1, then all äöüß will be shown strange ingame).
 * P.S.: The &quot;K&quot; at &quot;256K Storage&quot; shouldn&#x27;t be capitalized. The &quot;M&quot; for &quot;Mega&quot; is capitalized correctly, though.

[432fad972393840](https://git.the9grounds.com/minecraft/aeadditions/commit/432fad972393840) Vexatos *2013-08-03 06:44:04*


### Jira UTF-8   

**Create de_DE.xml**

 * I made this translation fit to the translation of AE.
 * Any suggestions are appreciated!
 * I hope, this has to be be UTF-8 encoded, otherwise I will change it (If it is NOT UTF-8 but ISO-8859-1, then all äöüß will be shown strange ingame).
 * P.S.: The &quot;K&quot; at &quot;256K Storage&quot; shouldn&#x27;t be capitalized. The &quot;M&quot; for &quot;Mega&quot; is capitalized correctly, though.

[432fad972393840](https://git.the9grounds.com/minecraft/aeadditions/commit/432fad972393840) Vexatos *2013-08-03 06:44:04*


### Jira patch-1   

**Merge pull request #75 from arition/patch-1**

 * Fix ME_Fluid_Import_Bus&amp;ME_Fluid_Export_Bus names

[f1544b76b2f7206](https://git.the9grounds.com/minecraft/aeadditions/commit/f1544b76b2f7206) Leon Loeser *2014-09-28 13:47:46*

**Merge pull request #76 from Adaptivity/patch-1**

 * Update ru_RU.lang

[4b35cb8c6184723](https://git.the9grounds.com/minecraft/aeadditions/commit/4b35cb8c6184723) Leon Loeser *2014-09-28 13:45:57*

**Merge pull request #23 from SinusoidalC/patch-1**

 * Create zh_CN.lang

[f3e24e03974db12](https://git.the9grounds.com/minecraft/aeadditions/commit/f3e24e03974db12) Leon Loeser *2014-05-26 07:46:57*

**Merge pull request #20 from AlgorithmX2/patch-1**

 * Use the security api correctly.

[14b8a191923cffa](https://git.the9grounds.com/minecraft/aeadditions/commit/14b8a191923cffa) Leon Loeser *2014-05-25 10:15:34*

**Merge pull request #11 from Adaptivity/patch-1**

 * Create ru_RU.lang

[9f6985238b181b9](https://git.the9grounds.com/minecraft/aeadditions/commit/9f6985238b181b9) Leon Loeser *2014-05-18 08:13:15*

**Merge pull request #2 from Vexatos/patch-1**

 * Update de_DE.lang

[fe686b4e79565fc](https://git.the9grounds.com/minecraft/aeadditions/commit/fe686b4e79565fc) M3gaFr3ak *2014-03-27 20:07:29*

**Merge pull request #91 from Vexatos/patch-1**

 * Leere. Puh.

[8dd21b72ae0855e](https://git.the9grounds.com/minecraft/aeadditions/commit/8dd21b72ae0855e) M3gaFr3ak *2013-12-09 17:39:56*

**Merge pull request #66 from Vexatos/patch-1**

 * -updated german localization

[5244ded5d6a3236](https://git.the9grounds.com/minecraft/aeadditions/commit/5244ded5d6a3236) M3gaFr3ak *2013-11-13 19:01:15*

**Merge pull request #64 from Vexatos/patch-1**

 * Update de_DE.xml

[f2f3a93f4e9ee3f](https://git.the9grounds.com/minecraft/aeadditions/commit/f2f3a93f4e9ee3f) M3gaFr3ak *2013-11-11 10:35:16*

**Merge pull request #58 from Vexatos/patch-1**

 * Update de_DE.xml

[53f03376b115223](https://git.the9grounds.com/minecraft/aeadditions/commit/53f03376b115223) M3gaFr3ak *2013-11-03 20:02:01*

**Merge pull request #50 from Vexatos/patch-1**

 * Added a ton of hyphens (It is German, remember)

[09bd55ec0e4e8a3](https://git.the9grounds.com/minecraft/aeadditions/commit/09bd55ec0e4e8a3) M3gaFr3ak *2013-10-22 16:12:07*

**Merge pull request #40 from Vexatos/patch-1**

 * Update de_DE.xml

[d2ef3e56216e56e](https://git.the9grounds.com/minecraft/aeadditions/commit/d2ef3e56216e56e) M3gaFr3ak *2013-09-23 19:56:01*

**Merge pull request #23 from Cisien/patch-1**

 * removed a redundant call to grid.getCellArray()

[4bb2d0079f5a53e](https://git.the9grounds.com/minecraft/aeadditions/commit/4bb2d0079f5a53e) M3gaFr3ak *2013-09-02 08:19:16*

**Merge pull request #18 from VeryBigBro/patch-1**

 * Russian localization

[c93949d7257adc4](https://git.the9grounds.com/minecraft/aeadditions/commit/c93949d7257adc4) M3gaFr3ak *2013-08-28 16:30:39*

**Merge pull request #9 from crafteverywhere/patch-1**

 * Create zh_CN.xml

[da34724bec235fa](https://git.the9grounds.com/minecraft/aeadditions/commit/da34724bec235fa) M3gaFr3ak *2013-08-12 18:23:52*

**Merge pull request #8 from Vexatos/patch-1**

 * Update de_DE.xml

[7155db7031b3e0f](https://git.the9grounds.com/minecraft/aeadditions/commit/7155db7031b3e0f) M3gaFr3ak *2013-08-11 17:17:45*

**Merge pull request #7 from Vexatos/patch-1**

 * Update de_DE.xml

[5192790cbc62ee2](https://git.the9grounds.com/minecraft/aeadditions/commit/5192790cbc62ee2) M3gaFr3ak *2013-08-11 16:38:28*

**Merge pull request #5 from Vexatos/patch-1**

 * Create de_DE.xml

[592eae0b6d5b304](https://git.the9grounds.com/minecraft/aeadditions/commit/592eae0b6d5b304) M3gaFr3ak *2013-08-03 07:33:15*


### Jira patch-2   

**Merge pull request #83 from Mazdallier/patch-2**

 * Create fr_FR.lang

[e0d7866bb362e3d](https://git.the9grounds.com/minecraft/aeadditions/commit/e0d7866bb362e3d) Leon Loeser *2014-10-14 15:14:53*

**Merge pull request #3 from Vexatos/patch-2**

 * Update de_DE.lang

[476d8a0f706f599](https://git.the9grounds.com/minecraft/aeadditions/commit/476d8a0f706f599) M3gaFr3ak *2014-04-14 09:21:19*

**Merge pull request #94 from SSCXM/patch-2**

 * Update zh_CN.xml

[3970f1adb987c9e](https://git.the9grounds.com/minecraft/aeadditions/commit/3970f1adb987c9e) M3gaFr3ak *2013-12-10 16:33:18*

**Merge pull request #59 from crafteverywhere/patch-2**

 * updated zh_CN.xml for ExtraCells 1.5.1

[85f2409b2fed57f](https://git.the9grounds.com/minecraft/aeadditions/commit/85f2409b2fed57f) M3gaFr3ak *2013-11-05 16:01:48*

**Merge pull request #54 from crafteverywhere/patch-2**

 * Update zh_CN.xml

[4da0eff5db844b4](https://git.the9grounds.com/minecraft/aeadditions/commit/4da0eff5db844b4) M3gaFr3ak *2013-11-03 20:01:50*

**Merge pull request #27 from crafteverywhere/patch-2**

 * The name of Chinese language file should be zh_CN

[19950f0a1a41db6](https://git.the9grounds.com/minecraft/aeadditions/commit/19950f0a1a41db6) M3gaFr3ak *2013-09-02 19:06:33*

**Merge pull request #26 from crafteverywhere/patch-2**

 * Update zh_CN.xml

[f41f52409de5b19](https://git.the9grounds.com/minecraft/aeadditions/commit/f41f52409de5b19) M3gaFr3ak *2013-09-02 18:53:24*

**Merge pull request #6 from Vexatos/patch-2**

 * Update de_DE.xml

[6742a38c069591a](https://git.the9grounds.com/minecraft/aeadditions/commit/6742a38c069591a) M3gaFr3ak *2013-08-10 20:45:16*


### Jira patch-3   

**Merge pull request #7 from Vexatos/patch-3**

 * Why you no hyphens D:

[00eb5d94b0c4ab5](https://git.the9grounds.com/minecraft/aeadditions/commit/00eb5d94b0c4ab5) M3gaFr3ak *2014-05-04 17:23:11*

**Merge pull request #35 from VeryBigBro/patch-3**

 * Update ru_RU.xml

[b1b5c74908d1edb](https://git.the9grounds.com/minecraft/aeadditions/commit/b1b5c74908d1edb) M3gaFr3ak *2013-09-24 15:35:52*


### Jira patch-4   

**Merge pull request #13 from Vexatos/patch-4**

 * Updated de_DE.xml

[49151cf0b774239](https://git.the9grounds.com/minecraft/aeadditions/commit/49151cf0b774239) M3gaFr3ak *2013-08-18 20:28:26*


### Jira patch-5   

**Merge pull request #31 from Vexatos/patch-5**

 * Update de_DE.xml

[fdd41d6e53130de](https://git.the9grounds.com/minecraft/aeadditions/commit/fdd41d6e53130de) M3gaFr3ak *2013-09-04 15:57:13*


### No issue

**Update version**


[0db2326a066611c](https://git.the9grounds.com/minecraft/aeadditions/commit/0db2326a066611c) DrummerMC *2015-02-02 15:04:36*

**Fixed crash**


[00c19ad8ba7f89a](https://git.the9grounds.com/minecraft/aeadditions/commit/00c19ad8ba7f89a) DrummerMC *2015-02-02 14:46:19*

**EC 2.2.49**

 * -Fixed Crafting PartFluidInterface

[0c08a7a8c73ee8a](https://git.the9grounds.com/minecraft/aeadditions/commit/0c08a7a8c73ee8a) DrummerMC *2015-01-29 19:09:51*

**EC 2.2.48**

 * - FluidAnnihilationPlane fix

[68362e9e4ad1115](https://git.the9grounds.com/minecraft/aeadditions/commit/68362e9e4ad1115) DrummerMC *2015-01-27 13:00:48*

**Block.Drain fix**

 * After having a play with some custom fluids I noticed that none of the IFluidBlock&#x27;s would work with the Annihilation Plane. It seems the wrong co-ordinates was being checked when using the &#x27;block.drain&#x27; function. I have made and tested the changes and they seem to work. Please give them a try.

[a800e50b69070fa](https://git.the9grounds.com/minecraft/aeadditions/commit/a800e50b69070fa) Sniperumm *2015-01-27 05:50:56*

**EC 2.2.47**


[77e6b5b87e1961c](https://git.the9grounds.com/minecraft/aeadditions/commit/77e6b5b87e1961c) DrummerMC *2015-01-25 21:34:10*

**Fixed build.gradle**


[3ce5ec8f9d3ce23](https://git.the9grounds.com/minecraft/aeadditions/commit/3ce5ec8f9d3ce23) DrummerMC *2015-01-24 14:32:15*

**Made fluid pattern WIP**


[18cf2c5aed636df](https://git.the9grounds.com/minecraft/aeadditions/commit/18cf2c5aed636df) Leon Loeser *2015-01-23 11:54:18*

**EC 2.2.44**

 * - More WAILA work
 * - bug fixes

[55265bbd1b726a3](https://git.the9grounds.com/minecraft/aeadditions/commit/55265bbd1b726a3) DrummerMC *2015-01-21 18:53:40*

**EC 2.2.43**

 * - More WAILA work
 * - Bug Fixes

[7e331fe125ff20e](https://git.the9grounds.com/minecraft/aeadditions/commit/7e331fe125ff20e) DrummerMC *2015-01-21 14:14:14*

**EC 2.2.42**

 * Added WAILA support

[730701cb8cd6ed1](https://git.the9grounds.com/minecraft/aeadditions/commit/730701cb8cd6ed1) DrummerMC *2015-01-20 19:10:49*

**Fixed Language**


[c7072916b50c1d3](https://git.the9grounds.com/minecraft/aeadditions/commit/c7072916b50c1d3) DrummerMC *2015-01-19 15:36:08*

**EC 2.2.40**

 * - Fixed de_DE.lang

[b9d8c7745e45004](https://git.the9grounds.com/minecraft/aeadditions/commit/b9d8c7745e45004) DrummerMC *2015-01-18 13:13:56*

**EC 2.2.39**

 * - Bug Fixes
 * - Added Ore Dictionary Export Bus

[7cd46efbd2bdfea](https://git.the9grounds.com/minecraft/aeadditions/commit/7cd46efbd2bdfea) DrummerMC *2015-01-18 13:09:12*

**EC 2.2.38**

 * -Bug Fixes

[e3c7c956a222f8a](https://git.the9grounds.com/minecraft/aeadditions/commit/e3c7c956a222f8a) DrummerMC *2015-01-17 17:49:46*

**EC 2.2.37**


[fecf067d1936494](https://git.the9grounds.com/minecraft/aeadditions/commit/fecf067d1936494) DrummerMC *2015-01-17 15:40:03*

**EC 2.2.36**

 * - Added Storage and Conversion Monitor
 * - Bug Fixes

[7c514ee69c9dec6](https://git.the9grounds.com/minecraft/aeadditions/commit/7c514ee69c9dec6) DrummerMC *2015-01-17 14:03:36*

**Texturen for DrummerMC**


[861db3751d1ed4b](https://git.the9grounds.com/minecraft/aeadditions/commit/861db3751d1ed4b) Alheimerjung *2015-01-16 23:42:50*

**Texture Update**


[097e2b32495916d](https://git.the9grounds.com/minecraft/aeadditions/commit/097e2b32495916d) DrummerMC *2015-01-16 21:45:37*

**EC 2.2.35**

 * Bug Fixes
 * Pattern Can now be placed in Fluid Interface

[810d414cf7bd44e](https://git.the9grounds.com/minecraft/aeadditions/commit/810d414cf7bd44e) DrummerMC *2015-01-16 21:41:10*

**EC 2.2.34**

 * Finished Fluid Auto Filler

[ad38ae82055a301](https://git.the9grounds.com/minecraft/aeadditions/commit/ad38ae82055a301) DrummerMC *2015-01-15 14:08:28*

**Fixed compile error**


[b9dcd4012f557c8](https://git.the9grounds.com/minecraft/aeadditions/commit/b9dcd4012f557c8) DrummerMC *2015-01-14 21:43:53*

**EC 2.2.33**

 * Fixed infinety crafting

[ba984282331ffcf](https://git.the9grounds.com/minecraft/aeadditions/commit/ba984282331ffcf) DrummerMC *2015-01-14 20:39:06*

**EC 2.2.32**

 * Some fixes if the world is not corectly load on tileentitys

[982665318ae9036](https://git.the9grounds.com/minecraft/aeadditions/commit/982665318ae9036) DrummerMC *2015-01-14 18:01:48*

**EC 2.2.31**


[3ebc836eff81848](https://git.the9grounds.com/minecraft/aeadditions/commit/3ebc836eff81848) DrummerMC *2015-01-13 21:30:16*

**EC 2.2.30**

 * Work on FluidFiller

[79391c240d22722](https://git.the9grounds.com/minecraft/aeadditions/commit/79391c240d22722) DrummerMC *2015-01-13 21:25:58*

**Update build.gradle**


[42f6e1ca91005e4](https://git.the9grounds.com/minecraft/aeadditions/commit/42f6e1ca91005e4) DrummerMC *2015-01-13 20:33:17*

**Fixed Build again**


[902ca2cf0027f12](https://git.the9grounds.com/minecraft/aeadditions/commit/902ca2cf0027f12) DrummerMC *2015-01-13 20:24:47*

**Fixed Build**


[f69340a719b7dda](https://git.the9grounds.com/minecraft/aeadditions/commit/f69340a719b7dda) DrummerMC *2015-01-13 20:03:02*

**EC 2.2.29**

 * remove api submodule because the api repesetory is not up to date
 * update bc api
 * work on fluid crafter

[cd71a2c2c29d948](https://git.the9grounds.com/minecraft/aeadditions/commit/cd71a2c2c29d948) DrummerMC *2015-01-13 19:58:00*

**EC 2.2.28**

 * Fixed TileEntityTick

[0c5c565cd87ae4d](https://git.the9grounds.com/minecraft/aeadditions/commit/0c5c565cd87ae4d) DrummerMC *2015-01-12 13:42:04*

**EC 2.2.26**


[771186eeccc5a86](https://git.the9grounds.com/minecraft/aeadditions/commit/771186eeccc5a86) DrummerMC *2015-01-11 20:07:31*

**Build Update**


[87a9ff603599fd9](https://git.the9grounds.com/minecraft/aeadditions/commit/87a9ff603599fd9) DrummerMC *2015-01-11 20:04:39*

**Merge branch 'rv2' of https://github.com/M3gaFr3ak/Extracells2 into rv2**


[488b844308221d4](https://git.the9grounds.com/minecraft/aeadditions/commit/488b844308221d4) DrummerMC *2015-01-11 20:03:07*

**ExtraCells 1.2.25**

 * Upscaled *side.png to 16*16

[0ffad16c300409f](https://git.the9grounds.com/minecraft/aeadditions/commit/0ffad16c300409f) Leon Loeser *2015-01-04 19:24:13*

**Working on fluid auto filler**


[d5bd6bdfea94a32](https://git.the9grounds.com/minecraft/aeadditions/commit/d5bd6bdfea94a32) DrummerMC *2014-12-24 12:16:01*

**EC 2.2.20**

 * Added ME Block Container

[445af94acd6b6ad](https://git.the9grounds.com/minecraft/aeadditions/commit/445af94acd6b6ad) DrummerMC *2014-12-20 10:00:02*

**Fixed versionnumber**


[c9c3a2f2e601d1c](https://git.the9grounds.com/minecraft/aeadditions/commit/c9c3a2f2e601d1c) Leon Loeser *2014-12-19 18:01:39*

**Added drone.io buildnumber**


[6aed724b3f5d00d](https://git.the9grounds.com/minecraft/aeadditions/commit/6aed724b3f5d00d) Leon Loeser *2014-12-19 17:54:07*

**ExtraCells 2.2.19**

 * -Added config for size-per-type

[ae4975ee9e760b3](https://git.the9grounds.com/minecraft/aeadditions/commit/ae4975ee9e760b3) Leon Loeser *2014-12-19 17:40:24*

**EC 2.2.18**

 * Fixed Crash when BC is not installed

[cfc128cd00a825b](https://git.the9grounds.com/minecraft/aeadditions/commit/cfc128cd00a825b) DrummerMC *2014-12-19 14:20:05*

**EC 2.2.16**

 * Network security work

[5ad0f8617ba7c63](https://git.the9grounds.com/minecraft/aeadditions/commit/5ad0f8617ba7c63) DrummerMC *2014-12-15 16:44:08*

**EC 2.2.15**

 * Fluid Interface(Block) and Fluid Assembler will now connect to networks with security

[8e45a0185363b0b](https://git.the9grounds.com/minecraft/aeadditions/commit/8e45a0185363b0b) DrummerMC *2014-12-14 11:57:24*

**EC 2.2.13**

 * Added RF support on Items

[f30b109a81aad7a](https://git.the9grounds.com/minecraft/aeadditions/commit/f30b109a81aad7a) DrummerMC *2014-12-13 14:27:10*

**Update README.md**


[2009480b3d4a0de](https://git.the9grounds.com/minecraft/aeadditions/commit/2009480b3d4a0de) DrummerMC *2014-12-13 10:50:25*

**EC 2.2.12**

 * Added Fluid Interface(Part)

[8be92493cb41333](https://git.the9grounds.com/minecraft/aeadditions/commit/8be92493cb41333) DrummerMC *2014-12-12 21:13:51*

**Fix code typos**


[ca35a5394422e65](https://git.the9grounds.com/minecraft/aeadditions/commit/ca35a5394422e65) DrummerMC *2014-12-12 18:15:57*

**Fix misspelled fields and methods in code**

 * Methods in ExtraCellsApi were instead deprecated and a properly-spelled
 * replacement added.

[e3e5e15d370565a](https://git.the9grounds.com/minecraft/aeadditions/commit/e3e5e15d370565a) riking *2014-12-12 17:56:42*

**Rename two methods in HandlerItemStorageFluid**

 * isPreformatted() -&gt; isFormatted() which also got an early exit
 * preformattedOrContainsFluid() -&gt; allowedByFormat()
 * (note that the former method name was 75% wrong before)

[8076c71815e284d](https://git.the9grounds.com/minecraft/aeadditions/commit/8076c71815e284d) riking *2014-12-12 17:54:53*

**EC 2.2.10**

 * Fluid Subnets now work
 * Added Idle Power Usage from Parts
 * Render Work on Fluid Level Emitter

[3b1ea1a6c4f2b69](https://git.the9grounds.com/minecraft/aeadditions/commit/3b1ea1a6c4f2b69) DrummerMC *2014-12-12 17:30:07*

**EC 2 2 8**

 * Added Fluid Interface(Block)
 * Make Blocks Wrenchable

[e041277c3aeceae](https://git.the9grounds.com/minecraft/aeadditions/commit/e041277c3aeceae) DrummerMC *2014-12-10 19:54:35*

**Update gradlew to version 2.0**


[3773f58d61ead66](https://git.the9grounds.com/minecraft/aeadditions/commit/3773f58d61ead66) Yip Rui Fung *2014-12-09 11:37:21*

**Merge branch 'rv2-emitterDupeFix' into rv2-fixes**


[79928b135162ff5](https://git.the9grounds.com/minecraft/aeadditions/commit/79928b135162ff5) Yip Rui Fung *2014-12-09 11:36:08*

**Fix localizations that depended on AE1 localizations.**


[e71abc0aa166721](https://git.the9grounds.com/minecraft/aeadditions/commit/e71abc0aa166721) Yip Rui Fung *2014-12-09 02:43:23*

**Changed crafting chamber name**


[cd5dbd821869545](https://git.the9grounds.com/minecraft/aeadditions/commit/cd5dbd821869545) DrummerMC *2014-12-08 17:52:29*

**Added Portable Fluid Cell**


[5a1fb00f6e42a2a](https://git.the9grounds.com/minecraft/aeadditions/commit/5a1fb00f6e42a2a) DrummerMC *2014-12-07 12:43:39*

**Wireless Term Api change and wireless term use now energy(rv2)**


[aff5cf181c0efa9](https://git.the9grounds.com/minecraft/aeadditions/commit/aff5cf181c0efa9) DrummerMC *2014-12-04 19:33:41*

**Added Fluid Crafting Chamber Receipe(rv1)**


[985c730eab774b8](https://git.the9grounds.com/minecraft/aeadditions/commit/985c730eab774b8) DrummerMC *2014-12-03 20:25:12*

**Fixed another NPE**


[9b8f5c809093857](https://git.the9grounds.com/minecraft/aeadditions/commit/9b8f5c809093857) Leon Loeser *2014-12-02 16:32:22*

**fix NPE**


[71cbf8a53ece9e1](https://git.the9grounds.com/minecraft/aeadditions/commit/71cbf8a53ece9e1) Leon Loeser *2014-12-02 15:51:28*

**(Hopefully) fixed channels nonsense**


[d21d8f513e7a2f9](https://git.the9grounds.com/minecraft/aeadditions/commit/d21d8f513e7a2f9)  *2014-12-02 15:13:27*

**Merge branch 'rv2' of https://github.com/M3gaFr3ak/ExtraCells2 into rv2**

 * Conflicts:
 * src/main/java/extracells/util/NameHandler.java
 * src/main/resources/assets/extracells/recipes/misc.recipe
 * src/main/resources/assets/extracells/recipes/parts.recipe

[57fa330df55a780](https://git.the9grounds.com/minecraft/aeadditions/commit/57fa330df55a780) Leon Loeser *2014-11-30 18:32:17*

**EC 2.2.5**

 * Fixed noncraftable items
 * Added fluidcrafting

[30f08f6a70b498e](https://git.the9grounds.com/minecraft/aeadditions/commit/30f08f6a70b498e) Leon Loeser *2014-11-30 18:26:49*

**Fixed Fluid Crafter(rv2)**


[879ec2ccd7d4e37](https://git.the9grounds.com/minecraft/aeadditions/commit/879ec2ccd7d4e37) DrummerMC *2014-11-30 10:15:32*

**Added Receips**


[2e49506ef8349f2](https://git.the9grounds.com/minecraft/aeadditions/commit/2e49506ef8349f2) DrummerMC *2014-11-19 11:41:49*

**Api Work(rv2)**


[5274f766b266870](https://git.the9grounds.com/minecraft/aeadditions/commit/5274f766b266870) DrummerMC *2014-11-17 19:36:01*

**Added Fluid Crafting Chamber on rv2**


[1632eaaa7281103](https://git.the9grounds.com/minecraft/aeadditions/commit/1632eaaa7281103) DrummerMC *2014-11-16 18:56:33*

**Version 2.2.4**

 * Fix Formationplane not saving config
 * Fix Parts not reacting to security breach

[40e4523fbea2a0c](https://git.the9grounds.com/minecraft/aeadditions/commit/40e4523fbea2a0c) Leon Loeser *2014-11-15 17:29:09*

**Version 2.2.3**

 * Temporary fix for parts only working in Ad-Hoc mode. Doesn&#x27;t use channels though

[b7c3eeaa38615d2](https://git.the9grounds.com/minecraft/aeadditions/commit/b7c3eeaa38615d2) Leon Loeser *2014-11-15 14:52:03*

**Version 2.2.2**

 * API!
 * Working wireless terminal

[2781acd79c5c9c2](https://git.the9grounds.com/minecraft/aeadditions/commit/2781acd79c5c9c2) Leon Loeser *2014-11-15 14:11:59*

**Added EC Api**


[9460c0666bbe05c](https://git.the9grounds.com/minecraft/aeadditions/commit/9460c0666bbe05c) DrummerMC *2014-11-15 10:08:13*

**Update Forge**


[55cd9df679e89d5](https://git.the9grounds.com/minecraft/aeadditions/commit/55cd9df679e89d5) DrummerMC *2014-11-15 10:07:55*

**Update AE Api**


[5858e210f8fea08](https://git.the9grounds.com/minecraft/aeadditions/commit/5858e210f8fea08) DrummerMC *2014-11-15 10:07:34*

**Update version**


[06b12d4f0bb9738](https://git.the9grounds.com/minecraft/aeadditions/commit/06b12d4f0bb9738) Leon Loeser *2014-11-13 15:15:51*

**Fix typo**


[04a5ced1d687201](https://git.the9grounds.com/minecraft/aeadditions/commit/04a5ced1d687201) Leon Loeser *2014-11-13 15:15:11*

**Wireless ME Fluid Terminal and the gui on ME Chest no works**


[91564f88e07e8aa](https://git.the9grounds.com/minecraft/aeadditions/commit/91564f88e07e8aa) DrummerMC *2014-11-11 15:59:53*

**Fixed cloning as simulations were not taken into account.**

 * Oh, derpy derpy derp.

[be8fce5ee831cc8](https://git.the9grounds.com/minecraft/aeadditions/commit/be8fce5ee831cc8) marcin212 *2014-10-31 20:00:30*

**Fixed partially duplicating fluid.**


[141ff394f861e0f](https://git.the9grounds.com/minecraft/aeadditions/commit/141ff394f861e0f) marcin212 *2014-10-31 15:50:36*

**Corrections on fr_FR.lang**


[d647866689ed866](https://git.the9grounds.com/minecraft/aeadditions/commit/d647866689ed866) Pyeroh&#x27;s Dev Concept *2014-10-20 05:19:08*

**Fixed a few mistakes in zh_CN.lang**


[bca678496f21aa9](https://git.the9grounds.com/minecraft/aeadditions/commit/bca678496f21aa9) sb023612 *2014-10-16 17:38:42*

**Create fr_FR.lang**


[e0b03647ccb4a9f](https://git.the9grounds.com/minecraft/aeadditions/commit/e0b03647ccb4a9f) Mazdallier *2014-10-14 13:53:40*

**Merge branch 'rv1' into rv2**


[73e988c36279210](https://git.the9grounds.com/minecraft/aeadditions/commit/73e988c36279210) Leon Loeser *2014-09-28 13:49:34*

**Update ru_RU.lang**


[b807686d958ede9](https://git.the9grounds.com/minecraft/aeadditions/commit/b807686d958ede9) Anton *2014-09-27 17:05:20*

**Update to AE2RV2**


[1dfa4ef77eb89e3](https://git.the9grounds.com/minecraft/aeadditions/commit/1dfa4ef77eb89e3) Leon Loeser *2014-09-27 16:28:50*

**Fix ME_Fluid_Import_Bus&ME_Fluid_Export_Bus names**


[6d0c7450465f0ea](https://git.the9grounds.com/minecraft/aeadditions/commit/6d0c7450465f0ea) arition *2014-09-27 02:13:01*

**User 'b' to seperate build number instead of '.'**


[8f3f844e719593b](https://git.the9grounds.com/minecraft/aeadditions/commit/8f3f844e719593b) Leon Loeser *2014-09-18 13:11:57*

**updated to add build.properties. updated build.gradle to include version numbering from both Jenkins and build.properties. added build.properties.**


[ec7ace75ce263ef](https://git.the9grounds.com/minecraft/aeadditions/commit/ec7ace75ce263ef) dmodoomsirius *2014-09-18 13:03:45*

**Fixed names for zh_CN.lang**


[e3f3938b8795a1b](https://git.the9grounds.com/minecraft/aeadditions/commit/e3f3938b8795a1b) sb023612 *2014-09-18 07:08:54*

**Updated API**


[3ebeb055eb72d75](https://git.the9grounds.com/minecraft/aeadditions/commit/3ebeb055eb72d75) Leon Loeser *2014-09-15 11:09:53*

**update api**


[98b0a3ea5c92fb2](https://git.the9grounds.com/minecraft/aeadditions/commit/98b0a3ea5c92fb2) Leon Loeser *2014-09-08 15:34:57*

**Fixed potential Dupe Glitch**


[cdd68051265accb](https://git.the9grounds.com/minecraft/aeadditions/commit/cdd68051265accb) Leon Loeser *2014-09-07 16:34:41*

**Fixed Localization (StorageCell/Component naming**


[ca756c181ccf0c3](https://git.the9grounds.com/minecraft/aeadditions/commit/ca756c181ccf0c3) Leon Loeser *2014-08-31 12:59:31*

**Merge branch 'rv1' of https://github.com/Wuestengecko/ExtraCells2 into Wuestengecko-rv1**


[213e10477d489b0](https://git.the9grounds.com/minecraft/aeadditions/commit/213e10477d489b0) Leon Loeser *2014-08-31 11:53:54*

**Fixed Fluid Terminal not accepting IFluidContainer, duplicating them (works w/ gregtech and ic2 now)**


[f34aceb79f37a61](https://git.the9grounds.com/minecraft/aeadditions/commit/f34aceb79f37a61) Leon Loeser *2014-08-31 11:45:03*

**Update zh_CN.lang**


[2111816abdaeac4](https://git.the9grounds.com/minecraft/aeadditions/commit/2111816abdaeac4) James *2014-08-25 15:03:30*

**Updated API and forge**


[c1ce8c81ee93bad](https://git.the9grounds.com/minecraft/aeadditions/commit/c1ce8c81ee93bad) M3gaFr3ak *2014-08-20 11:36:00*

**FluidTerm: Make max output stack size check more robust.**


[2ccc10f1f0c9aea](https://git.the9grounds.com/minecraft/aeadditions/commit/2ccc10f1f0c9aea) Wuestengecko *2014-08-11 19:51:00*

**Updated API**


[9f0e73d255d11e6](https://git.the9grounds.com/minecraft/aeadditions/commit/9f0e73d255d11e6) M3gaFr3ak *2014-07-28 15:31:36*

**Added recipes**


[489d25b73fbb8df](https://git.the9grounds.com/minecraft/aeadditions/commit/489d25b73fbb8df) M3gaFr3ak *2014-07-25 20:50:44*

**Update HandlerItemStorageFluid.java**

 * Prevent NPE(http://paste.ee/p/Uf2ER)

[a9a06f142712776](https://git.the9grounds.com/minecraft/aeadditions/commit/a9a06f142712776) Vilim Lendvaj *2014-07-22 19:14:41*

**Texture Improvements**


[ea0b2895d34058b](https://git.the9grounds.com/minecraft/aeadditions/commit/ea0b2895d34058b) M3gaFr3ak *2014-07-18 14:58:46*

**Update API**


[c9184b859e38954](https://git.the9grounds.com/minecraft/aeadditions/commit/c9184b859e38954) M3gaFr3ak *2014-07-17 18:36:52*

**Port to rv1**


[2bee1e307f4603e](https://git.the9grounds.com/minecraft/aeadditions/commit/2bee1e307f4603e) M3gaFr3ak *2014-07-16 16:02:02*

**Update to RV1**


[abca09ce8261bd1](https://git.the9grounds.com/minecraft/aeadditions/commit/abca09ce8261bd1) M3gaFr3ak *2014-07-16 15:10:56*

**WorldLoadFix Versionnumber**


[c372370ffa26d46](https://git.the9grounds.com/minecraft/aeadditions/commit/c372370ffa26d46) M3gaFr3ak *2014-07-15 07:49:11*

**Merge branch 'Nividica-master'**


[aa413e57b286075](https://git.the9grounds.com/minecraft/aeadditions/commit/aa413e57b286075) M3gaFr3ak *2014-07-15 07:47:20*

**World load fix for storage bus.**

 * Issue: The storage bus does not update the network on world load. The network must be changed in some way before storage buses report fluid contents.
 * This should correct the issue and allow the bus to report its containers contents as the network issues the channel changes.

[66d7d27c75640ea](https://git.the9grounds.com/minecraft/aeadditions/commit/66d7d27c75640ea) Chris *2014-07-14 19:00:06*

**Added Version number for redstone fix**


[8cd7778b4ec6a64](https://git.the9grounds.com/minecraft/aeadditions/commit/8cd7778b4ec6a64) M3gaFr3ak *2014-07-09 13:27:19*

**Fixed Names**


[33232e9499bdbe2](https://git.the9grounds.com/minecraft/aeadditions/commit/33232e9499bdbe2) M3gaFr3ak *2014-06-28 14:22:24*

**Added walrus; First milestone (apart from all the other stuff...)**


[96ba1e67a51d7c5](https://git.the9grounds.com/minecraft/aeadditions/commit/96ba1e67a51d7c5) M3gaFr3ak *2014-06-27 20:30:51*

**Update API, Update forge**


[8c3ba52c838f63b](https://git.the9grounds.com/minecraft/aeadditions/commit/8c3ba52c838f63b) M3gaFr3ak *2014-06-26 18:13:14*

**Fixed bad recipe**


[21af317525c9395](https://git.the9grounds.com/minecraft/aeadditions/commit/21af317525c9395) M3gaFr3ak *2014-06-24 13:47:30*

**Added sideCheck**


[e4e9a4a15722ddd](https://git.the9grounds.com/minecraft/aeadditions/commit/e4e9a4a15722ddd) M3gaFr3ak *2014-06-19 21:33:49*

**Added nullcheck for safety^^**


[e7ae6a63973772f](https://git.the9grounds.com/minecraft/aeadditions/commit/e7ae6a63973772f) M3gaFr3ak *2014-06-19 12:30:19*

**Fix terminal namerendering, Add storagebus recipe**


[0e7ddcb1b3be76e](https://git.the9grounds.com/minecraft/aeadditions/commit/0e7ddcb1b3be76e) M3gaFr3ak *2014-06-14 16:14:44*

**Update API, fix Stuffs.**


[89d9fe7f15d6b94](https://git.the9grounds.com/minecraft/aeadditions/commit/89d9fe7f15d6b94) M3gaFr3ak *2014-06-11 19:46:39*

**Fix upgrade detection**


[f68868f8e3789f4](https://git.the9grounds.com/minecraft/aeadditions/commit/f68868f8e3789f4) M3gaFr3ak *2014-06-09 10:07:26*

**fix fix (temporary w/ try)**


[95ddb2574f11110](https://git.the9grounds.com/minecraft/aeadditions/commit/95ddb2574f11110) M3gaFr3ak *2014-06-08 17:46:11*

**Fixed Crash on wrenched parts**


[8c4f16d890b8f12](https://git.the9grounds.com/minecraft/aeadditions/commit/8c4f16d890b8f12) M3gaFr3ak *2014-06-08 17:40:03*

**Fixed network crash**


[d89df0bdbc4e9e5](https://git.the9grounds.com/minecraft/aeadditions/commit/d89df0bdbc4e9e5) M3gaFr3ak *2014-06-08 13:27:01*

**Rewrite of PacketSystem due to old one being bugged**


[0761205f64bd140](https://git.the9grounds.com/minecraft/aeadditions/commit/0761205f64bd140) M3gaFr3ak *2014-06-08 09:08:26*

**Fixed crash**


[434ce67f4df3924](https://git.the9grounds.com/minecraft/aeadditions/commit/434ce67f4df3924) M3gaFr3ak *2014-06-07 22:56:49*

**Fixed SMP GUI Crash**


[e9dd8b30332a172](https://git.the9grounds.com/minecraft/aeadditions/commit/e9dd8b30332a172) M3gaFr3ak *2014-06-07 18:56:13*

**API update, ME Drive Fixture**

 * -Updated API
 * -Added breaking behaviour for ME Drive Fixture to drop its contents if pickaxed

[31b57d8ce9f578f](https://git.the9grounds.com/minecraft/aeadditions/commit/31b57d8ce9f578f) M3gaFr3ak *2014-06-05 18:54:10*

**Fix FluidLevelEmitter**


[6bdb2d1b62274ea](https://git.the9grounds.com/minecraft/aeadditions/commit/6bdb2d1b62274ea) M3gaFr3ak *2014-06-02 17:32:06*

**Merge remote-tracking branch 'Wuestengecko/master'**


[adf3fe094ea2f03](https://git.the9grounds.com/minecraft/aeadditions/commit/adf3fe094ea2f03) M3gaFr3ak *2014-06-02 17:30:16*

**Remove build number completely**


[bb0160d7ca40752](https://git.the9grounds.com/minecraft/aeadditions/commit/bb0160d7ca40752) M3gaFr3ak *2014-06-02 14:13:46*

**-Fixed NPE**

 * -Removed buildnumber due to not making sense w/ multiple asynchronous CIs

[4031304d1e0c8ef](https://git.the9grounds.com/minecraft/aeadditions/commit/4031304d1e0c8ef) M3gaFr3ak *2014-06-02 14:01:09*

**Fluid Level Emitter: Fix text input box (Protocol change!)**


[646f69911130236](https://git.the9grounds.com/minecraft/aeadditions/commit/646f69911130236) Wuestengecko *2014-05-28 07:06:34*

**Fluid Level Emitter: Properly update redstone**


[805907b93fd49e8](https://git.the9grounds.com/minecraft/aeadditions/commit/805907b93fd49e8) U-Wuestengecko-PC\Wuestengecko *2014-05-28 05:21:42*

**Fixed Fluid Terminal working w/o power**


[0b45411462b1f68](https://git.the9grounds.com/minecraft/aeadditions/commit/0b45411462b1f68) M3gaFr3ak *2014-05-27 15:41:03*

**Create zh_CN.lang**

 * Ok, finish that!

[55005bfc76f4364](https://git.the9grounds.com/minecraft/aeadditions/commit/55005bfc76f4364) SinusoidalC *2014-05-26 07:42:42*

**ME Chest, Fluid Terminal**

 * Fixed ME Chest Security Problem
 * Added Damage and Energy to the Wireless Fluid Terminal

[cfc8c2c1f6c6c6d](https://git.the9grounds.com/minecraft/aeadditions/commit/cfc8c2c1f6c6c6d) M3gaFr3ak *2014-05-25 11:31:07*

**Use the security api correctly.**


[961c0b6663024fa](https://git.the9grounds.com/minecraft/aeadditions/commit/961c0b6663024fa) AlgorithmX2 *2014-05-24 23:13:14*

**revert BS**


[b5b85fd97e304ef](https://git.the9grounds.com/minecraft/aeadditions/commit/b5b85fd97e304ef) M3gaFr3ak *2014-05-24 22:01:42*

**Testing stuff**


[f4c9aacfc95545b](https://git.the9grounds.com/minecraft/aeadditions/commit/f4c9aacfc95545b) M3gaFr3ak *2014-05-24 21:51:00*

**Add drone.io buildnumber**


[6952d202bb70c4f](https://git.the9grounds.com/minecraft/aeadditions/commit/6952d202bb70c4f) M3gaFr3ak *2014-05-24 21:47:29*

**fix of buildfile**


[dc522f708192312](https://git.the9grounds.com/minecraft/aeadditions/commit/dc522f708192312) M3gaFr3ak *2014-05-24 21:44:01*

**Fix stack errors**


[912c6dccc1d3404](https://git.the9grounds.com/minecraft/aeadditions/commit/912c6dccc1d3404) M3gaFr3ak *2014-05-24 21:42:05*

**update buildscript**


[2fba319bf7bf91c](https://git.the9grounds.com/minecraft/aeadditions/commit/2fba319bf7bf91c) M3gaFr3ak *2014-05-24 21:14:47*

**update build.gradle to work with jenkins**


[c1f4c6340cb593e](https://git.the9grounds.com/minecraft/aeadditions/commit/c1f4c6340cb593e) dmodoomsirius *2014-05-23 23:16:38*

**Rollback gradle merge, needs redo**


[de3e5800179bf3a](https://git.the9grounds.com/minecraft/aeadditions/commit/de3e5800179bf3a) M3gaFr3ak *2014-05-23 22:32:17*

**Fix World Crash**


[fd1adf30e7f5560](https://git.the9grounds.com/minecraft/aeadditions/commit/fd1adf30e7f5560) M3gaFr3ak *2014-05-23 22:22:03*

**Merge gradle**


[c4ee1ed0f7c9db0](https://git.the9grounds.com/minecraft/aeadditions/commit/c4ee1ed0f7c9db0) M3gaFr3ak *2014-05-23 22:21:44*

**update build.gradle to work with jenkins**


[14ee3a4a82d99ee](https://git.the9grounds.com/minecraft/aeadditions/commit/14ee3a4a82d99ee) dmodoomsirius *2014-05-23 21:56:13*

**Fix serverside crashes!**


[c63f91baf12926c](https://git.the9grounds.com/minecraft/aeadditions/commit/c63f91baf12926c) M3gaFr3ak *2014-05-23 15:47:52*

**Fixed import bug**


[6dfab451583e915](https://git.the9grounds.com/minecraft/aeadditions/commit/6dfab451583e915) M3gaFr3ak *2014-05-23 15:13:38*

**New Versionnumber, Textures, Fixes, Rarity!**


[218573091d18044](https://git.the9grounds.com/minecraft/aeadditions/commit/218573091d18044) M3gaFr3ak *2014-05-23 14:47:30*

**update api**


[5bdb3912cd19e71](https://git.the9grounds.com/minecraft/aeadditions/commit/5bdb3912cd19e71) M3gaFr3ak *2014-05-20 20:34:52*

**Fixed File**

 * Encoding wasnt the problem, the backslash was

[fbe6bf88e9ecaf4](https://git.the9grounds.com/minecraft/aeadditions/commit/fbe6bf88e9ecaf4) M3gaFr3ak *2014-05-18 13:35:57*

**Fixed encoding?**


[02cfd1216f34e7e](https://git.the9grounds.com/minecraft/aeadditions/commit/02cfd1216f34e7e) M3gaFr3ak *2014-05-18 13:35:17*

**small typo fixed**


[126b5e3bd019680](https://git.the9grounds.com/minecraft/aeadditions/commit/126b5e3bd019680) Anton *2014-05-18 06:44:26*

**Create ru_RU.lang**


[581748dc9d59359](https://git.the9grounds.com/minecraft/aeadditions/commit/581748dc9d59359) Anton *2014-05-18 06:43:36*

**Fixed "not tessellating" error**


[02a1d000489b1de](https://git.the9grounds.com/minecraft/aeadditions/commit/02a1d000489b1de) M3gaFr3ak *2014-05-12 16:20:34*

**Fixed buildfile formatting**


[35ea22af12902f4](https://git.the9grounds.com/minecraft/aeadditions/commit/35ea22af12902f4) M3gaFr3ak *2014-05-11 19:25:30*

**Reformatted everything, another minor change i forgot**


[fafa6e0d19b4e3b](https://git.the9grounds.com/minecraft/aeadditions/commit/fafa6e0d19b4e3b) M3gaFr3ak *2014-05-11 19:18:58*

**Fixed Pattern Rendering**


[b4ab8efec449448](https://git.the9grounds.com/minecraft/aeadditions/commit/b4ab8efec449448) M3gaFr3ak *2014-05-08 19:12:59*

**Merge branch 'master' of https://github.com/M3gaFr3ak/ExtraCells2**


[87985edadc6269c](https://git.the9grounds.com/minecraft/aeadditions/commit/87985edadc6269c) M3gaFr3ak *2014-05-06 16:17:54*

**Cell/fluidpattern rightclick,tank rendering**

 * -Cells eject their components on shift-rightclick
 * -Tank now renders correctly

[1da1aecdad2dfe9](https://git.the9grounds.com/minecraft/aeadditions/commit/1da1aecdad2dfe9) M3gaFr3ak *2014-05-06 16:17:17*

**Why you no hyphens D:**

 * Proper grammar be impotant in German

[e01d13791c39c5c](https://git.the9grounds.com/minecraft/aeadditions/commit/e01d13791c39c5c) Vexatos *2014-05-04 17:15:11*

**recipes for parts**


[05edd30d0688798](https://git.the9grounds.com/minecraft/aeadditions/commit/05edd30d0688798) M3gaFr3ak *2014-05-02 18:33:24*

**More Recipes! And renaming of storagecomponents**


[79f8829e649a6f6](https://git.the9grounds.com/minecraft/aeadditions/commit/79f8829e649a6f6) M3gaFr3ak *2014-05-01 17:03:43*

**Crafting Recipes (some of them) and Fluid Pattern**


[019f9c3122ce605](https://git.the9grounds.com/minecraft/aeadditions/commit/019f9c3122ce605) M3gaFr3ak *2014-04-30 17:32:40*

**NPE fix**


[7fba45184892727](https://git.the9grounds.com/minecraft/aeadditions/commit/7fba45184892727) M3gaFr3ak *2014-04-30 11:24:52*

**Made "Gui" methods become "object" methods, so the server doesnt crash >.<**


[270e76b53818512](https://git.the9grounds.com/minecraft/aeadditions/commit/270e76b53818512) M3gaFr3ak *2014-04-29 19:09:18*

**Update to Forge 10.12.1.1063**


[664befac39f1503](https://git.the9grounds.com/minecraft/aeadditions/commit/664befac39f1503) M3gaFr3ak *2014-04-29 18:19:12*

**-Fixed/Recoded Gui System**

 * -Updated to AE2 build 99

[03e8c8732dc0e61](https://git.the9grounds.com/minecraft/aeadditions/commit/03e8c8732dc0e61) M3gaFr3ak *2014-04-29 12:28:13*

**Own Gui networking since i hate the native :D**


[67e57a44444b54b](https://git.the9grounds.com/minecraft/aeadditions/commit/67e57a44444b54b) M3gaFr3ak *2014-04-14 12:56:29*

**Update de_DE.lang**


[08e47165a29f185](https://git.the9grounds.com/minecraft/aeadditions/commit/08e47165a29f185) Vexatos *2014-04-14 09:20:57*

**Updated forge and api**


[d6cb4eea006e7e0](https://git.the9grounds.com/minecraft/aeadditions/commit/d6cb4eea006e7e0) M3gaFr3ak *2014-04-12 09:49:55*

**Update API**


[7df192f2ce73e1e](https://git.the9grounds.com/minecraft/aeadditions/commit/7df192f2ce73e1e) M3gaFr3ak *2014-04-04 19:03:37*

**compile fix**


[5daf231185c0c61](https://git.the9grounds.com/minecraft/aeadditions/commit/5daf231185c0c61) M3gaFr3ak *2014-03-28 18:34:59*

**Tooltip for storage cell, other stuff**


[483f353ee385d26](https://git.the9grounds.com/minecraft/aeadditions/commit/483f353ee385d26) M3gaFr3ak *2014-03-28 18:25:31*

**Update de_DE.lang**


[bba9bfffe51cfe2](https://git.the9grounds.com/minecraft/aeadditions/commit/bba9bfffe51cfe2) Vexatos *2014-03-27 19:08:58*

**More Fluidcells, textures, fixes, refactoring**


[49eb907df5e2b82](https://git.the9grounds.com/minecraft/aeadditions/commit/49eb907df5e2b82) M3gaFr3ak *2014-03-27 17:02:16*

**FluidTerminal fixes, will soon be functional**


[95ff8a934085388](https://git.the9grounds.com/minecraft/aeadditions/commit/95ff8a934085388) M3gaFr3ak *2014-03-22 16:31:14*

**Fluid Terminal Gui fixes**


[dc29136cf73eba9](https://git.the9grounds.com/minecraft/aeadditions/commit/dc29136cf73eba9) M3gaFr3ak *2014-03-15 15:58:58*

**readded gradleWrapper**


[df1ece20f4461fc](https://git.the9grounds.com/minecraft/aeadditions/commit/df1ece20f4461fc) M3gaFr3ak *2014-03-14 16:43:34*

**Gui Refactoring, Level Emitter Slot**

 * -Made GuiUtil class for simple stuffs
 * -Level Emitter Gui has a FluidSlot now

[ff0d1952f18d974](https://git.the9grounds.com/minecraft/aeadditions/commit/ff0d1952f18d974) M3gaFr3ak *2014-03-14 16:20:22*

**Packer refactoring, LevelEmitter gui**


[0b06358b53d694c](https://git.the9grounds.com/minecraft/aeadditions/commit/0b06358b53d694c) M3gaFr3ak *2014-03-12 15:05:28*

**License change, PartBattery edits**

 * -MIT License!
 * -PartBattery is not dependend of the battery in it.

[0153e3dd0f23e4f](https://git.the9grounds.com/minecraft/aeadditions/commit/0153e3dd0f23e4f) M3gaFr3ak *2014-03-11 19:38:56*

**Render Fixes, cleanup, Battery**


[b4127da8d883281](https://git.the9grounds.com/minecraft/aeadditions/commit/b4127da8d883281) M3gaFr3ak *2014-03-06 19:38:23*

**AE API update**


[3fe202d6cab9e76](https://git.the9grounds.com/minecraft/aeadditions/commit/3fe202d6cab9e76) M3gaFr3ak *2014-02-28 08:20:08*

**forge update, ME Drive part**


[322f6abfb760c05](https://git.the9grounds.com/minecraft/aeadditions/commit/322f6abfb760c05) M3gaFr3ak *2014-02-26 14:39:46*

**ME Drive, Refactoring**

 * -Added ME Drive Part
 * -Refactored stuff
 * -Textures

[32f47a91fb676ee](https://git.the9grounds.com/minecraft/aeadditions/commit/32f47a91fb676ee) M3gaFr3ak *2014-02-24 14:14:51*

**updated ae api**


[af08af4924e6ae4](https://git.the9grounds.com/minecraft/aeadditions/commit/af08af4924e6ae4) M3gaFr3ak *2014-02-24 10:14:56*

**Shortened Buckets, Guis, Work**

 * -Shortened Buckets reintroduced
 * -Fluid Storage has Gui now
 * -Bugfixes
 * -Refactoring

[a3fa500ee45350a](https://git.the9grounds.com/minecraft/aeadditions/commit/a3fa500ee45350a) M3gaFr3ak *2014-02-23 13:37:20*

**AE API updates, fixes**


[c0993a99717e09f](https://git.the9grounds.com/minecraft/aeadditions/commit/c0993a99717e09f) M3gaFr3ak *2014-02-21 21:41:55*

**fix**


[8fcafad5cc21abf](https://git.the9grounds.com/minecraft/aeadditions/commit/8fcafad5cc21abf) M3gaFr3ak *2014-02-21 19:57:15*

**Merge branch 'master' of https://github.com/M3gaFr3ak/ExtraCells2**


[46ce606f29f9e39](https://git.the9grounds.com/minecraft/aeadditions/commit/46ce606f29f9e39) M3gaFr3ak *2014-02-21 19:55:16*

**Updated AE2 API**


[8bf4784a604bed2](https://git.the9grounds.com/minecraft/aeadditions/commit/8bf4784a604bed2) M3gaFr3ak *2014-02-21 19:54:36*

**Cleanup**

 * -Edited FluidUtil
 * -AnnihilationPlane fixes

[dc7b200461adee3](https://git.the9grounds.com/minecraft/aeadditions/commit/dc7b200461adee3) M3gaFr3ak *2014-02-21 19:03:53*

**Tab Localization, Plane Lights, Plane localization**

 * -Fixed Creative Tab Localization
 * -Added Lights to Planes
 * -Fixed Plane Localization
 * -Added plane itemgroup

[6fba91d1429f2d2](https://git.the9grounds.com/minecraft/aeadditions/commit/6fba91d1429f2d2) M3gaFr3ak *2014-02-19 15:00:15*

**Updated AE2 API**


[19c338964974e1e](https://git.the9grounds.com/minecraft/aeadditions/commit/19c338964974e1e) M3gaFr3ak *2014-02-19 14:38:48*

**Render Fixes, Pane addition, Terminal works**

 * -Fluid Annihilation Pane functional
 * -Fluid Formation Pane functional
 * -Fluid Terminal works
 * -Render fixes

[862767e60855b85](https://git.the9grounds.com/minecraft/aeadditions/commit/862767e60855b85) M3gaFr3ak *2014-02-19 12:48:24*

**Lighting Fixes**

 * -getLightLevel uses isActive() now
 * -Parts only flow if they&#x27;re active

[f4449a4c92d1bc9](https://git.the9grounds.com/minecraft/aeadditions/commit/f4449a4c92d1bc9) M3gaFr3ak *2014-02-15 22:21:51*

**Fixed Import**


[be25cd330b0fbbf](https://git.the9grounds.com/minecraft/aeadditions/commit/be25cd330b0fbbf) M3gaFr3ak *2014-02-15 15:30:05*

**Fluid Annihilation Pane**

 * -Adds Part for Fluid Annihilation Pane
 * -Storage Bus respects Inverter

[de7fcf29ddf172c](https://git.the9grounds.com/minecraft/aeadditions/commit/de7fcf29ddf172c) M3gaFr3ak *2014-02-15 14:57:06*

**Merge branch 'master' of https://github.com/M3gaFr3ak/ExtraCells2**


[fd39d3ef333e089](https://git.the9grounds.com/minecraft/aeadditions/commit/fd39d3ef333e089) M3gaFr3ak *2014-02-15 12:41:28*

**Updated API**

 * -Updates AE2 API

[358f2159a5d69a3](https://git.the9grounds.com/minecraft/aeadditions/commit/358f2159a5d69a3) M3gaFr3ak *2014-02-15 12:41:07*

**Update README.md**


[7e2628d1b9cba01](https://git.the9grounds.com/minecraft/aeadditions/commit/7e2628d1b9cba01) M3gaFr3ak *2014-02-14 23:12:19*

**Removing useless stuff**


[57bd7878d1f8930](https://git.the9grounds.com/minecraft/aeadditions/commit/57bd7878d1f8930) M3gaFr3ak *2014-02-14 22:45:31*

**Merge branch 'master' of https://github.com/M3gaFr3ak/ExtraCells2**


[eb5a0925145fd3a](https://git.the9grounds.com/minecraft/aeadditions/commit/eb5a0925145fd3a) M3gaFr3ak *2014-02-14 19:06:04*

**Scre JavaFX Pairs!**


[a714e3ee8450ff1](https://git.the9grounds.com/minecraft/aeadditions/commit/a714e3ee8450ff1) M3gaFr3ak *2014-02-14 19:05:47*

**Update .travis.yml**


[8c678c0fe6da8a4](https://git.the9grounds.com/minecraft/aeadditions/commit/8c678c0fe6da8a4) M3gaFr3ak *2014-02-14 18:55:37*

**Submodules**


[73b008da866d0c3](https://git.the9grounds.com/minecraft/aeadditions/commit/73b008da866d0c3) M3gaFr3ak *2014-02-14 18:52:12*

**update jkd**


[0fd93d1cf92063e](https://git.the9grounds.com/minecraft/aeadditions/commit/0fd93d1cf92063e) M3gaFr3ak *2014-02-14 18:47:53*

**changes, hopefully it'll work XD**


[19c53e8ad3b4133](https://git.the9grounds.com/minecraft/aeadditions/commit/19c53e8ad3b4133) M3gaFr3ak *2014-02-14 18:45:31*

**another travis commit XD**


[c4421cd4b7d1fe1](https://git.the9grounds.com/minecraft/aeadditions/commit/c4421cd4b7d1fe1) M3gaFr3ak *2014-02-14 18:39:06*

**travis**


[095f70891084f22](https://git.the9grounds.com/minecraft/aeadditions/commit/095f70891084f22) M3gaFr3ak *2014-02-14 18:32:03*

**travis...**


[f23ea9573dced67](https://git.the9grounds.com/minecraft/aeadditions/commit/f23ea9573dced67) M3gaFr3ak *2014-02-14 18:27:11*

**Added CI test**


[76f2805b35dd56c](https://git.the9grounds.com/minecraft/aeadditions/commit/76f2805b35dd56c) M3gaFr3ak *2014-02-14 18:22:25*

**Generalization**

 * -Made Gui for Storage Bus
 * -Made FluidSlot Packets
 * -Generic stuff for FluidSlots

[f8978c13d80f989](https://git.the9grounds.com/minecraft/aeadditions/commit/f8978c13d80f989) M3gaFr3ak *2014-02-14 15:52:48*

**Fluid Terminal drains containers**


[3aa11eca89d19c2](https://git.the9grounds.com/minecraft/aeadditions/commit/3aa11eca89d19c2) M3gaFr3ak *2014-02-14 14:00:28*

**-Cooler glowy parts**


[5f475cb613fb57e](https://git.the9grounds.com/minecraft/aeadditions/commit/5f475cb613fb57e) M3gaFr3ak *2014-02-13 20:34:55*

**-Added Lights to StorageBus**


[10a4f645b9b9e3e](https://git.the9grounds.com/minecraft/aeadditions/commit/10a4f645b9b9e3e) M3gaFr3ak *2014-02-13 17:48:29*

**-Added Textures (Actually forgot to add them to the last commit)**


[57c31206b4cbc02](https://git.the9grounds.com/minecraft/aeadditions/commit/57c31206b4cbc02) M3gaFr3ak *2014-02-13 17:16:30*

**-Added Bus Lights**

 * -Added Terminal Rendering

[0114ad4e8a2cb2c](https://git.the9grounds.com/minecraft/aeadditions/commit/0114ad4e8a2cb2c) M3gaFr3ak *2014-02-13 17:15:06*

**Some cleaning up**


[3b19f4c1a43f03b](https://git.the9grounds.com/minecraft/aeadditions/commit/3b19f4c1a43f03b) M3gaFr3ak *2014-02-12 16:33:31*

**level emitter changes**


[66686a13dbcfdf0](https://git.the9grounds.com/minecraft/aeadditions/commit/66686a13dbcfdf0) M3gaFr3ak *2014-02-12 15:14:59*

**Update API**


[bea36c042652b3f](https://git.the9grounds.com/minecraft/aeadditions/commit/bea36c042652b3f) M3gaFr3ak *2014-02-12 08:44:37*

**PartEnum for convenience**

 * Localizations
 * Other stuff

[f0f331f36d87981](https://git.the9grounds.com/minecraft/aeadditions/commit/f0f331f36d87981) M3gaFr3ak *2014-02-11 20:24:29*

**forgot the slot...**


[8fa60a0a341fa56](https://git.the9grounds.com/minecraft/aeadditions/commit/8fa60a0a341fa56) M3gaFr3ak *2014-02-10 18:49:50*

**network tool working**


[acc50a0cf9a0325](https://git.the9grounds.com/minecraft/aeadditions/commit/acc50a0cf9a0325) M3gaFr3ak *2014-02-10 18:49:12*

**Upgrade dependent Speed**

 * Upgrade dependent Redstone controll

[a03cac59b91cc59](https://git.the9grounds.com/minecraft/aeadditions/commit/a03cac59b91cc59) M3gaFr3ak *2014-02-10 11:05:05*

**handle slots with network tool :D**


[2360b400cb201b1](https://git.the9grounds.com/minecraft/aeadditions/commit/2360b400cb201b1) M3gaFr3ak *2014-02-09 20:16:16*

**Many Gui Changes, update to 1.7.2; functional stuff, awesome packets**


[56dd3c344f0e36e](https://git.the9grounds.com/minecraft/aeadditions/commit/56dd3c344f0e36e) M3gaFr3ak *2014-02-09 18:43:05*

**Merge branch 'master' of https://github.com/M3gaFr3ak/ExtraCells2**


[c0079ed78c4a443](https://git.the9grounds.com/minecraft/aeadditions/commit/c0079ed78c4a443) M3gaFr3ak *2014-02-09 11:09:36*

**new stuffs**


[af83c598b4bfe61](https://git.the9grounds.com/minecraft/aeadditions/commit/af83c598b4bfe61) M3gaFr3ak *2014-02-09 11:06:45*

**preparation**


[ed186c859cefaf6](https://git.the9grounds.com/minecraft/aeadditions/commit/ed186c859cefaf6) M3gaFr3ak *2014-02-09 11:05:52*

**Initial commit**


[ad43087f5f961ca](https://git.the9grounds.com/minecraft/aeadditions/commit/ad43087f5f961ca) M3gaFr3ak *2014-02-09 11:00:03*

**Added config slots**


[2fc45d74728d620](https://git.the9grounds.com/minecraft/aeadditions/commit/2fc45d74728d620) M3gaFr3ak *2014-02-08 16:38:57*

**Working IOPart Guis! Working on TerminalGui now...**


[07db097f31bc0a9](https://git.the9grounds.com/minecraft/aeadditions/commit/07db097f31bc0a9) M3gaFr3ak *2014-02-07 23:04:30*

**Beginning of IO Port gui**


[79dcf3efcefae0b](https://git.the9grounds.com/minecraft/aeadditions/commit/79dcf3efcefae0b) M3gaFr3ak *2014-02-07 15:59:02*

**Un-Raw types**

 * beautiful explicity!

[3d06716e2946dac](https://git.the9grounds.com/minecraft/aeadditions/commit/3d06716e2946dac) M3gaFr3ak *2014-02-06 16:33:08*

**update api**


[25b6c5550650020](https://git.the9grounds.com/minecraft/aeadditions/commit/25b6c5550650020) M3gaFr3ak *2014-02-06 16:30:52*

**fluid storage cell, builscript fix**


[5c17256a828f3e9](https://git.the9grounds.com/minecraft/aeadditions/commit/5c17256a828f3e9) M3gaFr3ak *2014-02-06 15:47:56*

**sync**


[483bb398d063e08](https://git.the9grounds.com/minecraft/aeadditions/commit/483bb398d063e08) M3gaFr3ak *2014-02-05 09:07:48*

**changes**


[1bce0d51b0c560a](https://git.the9grounds.com/minecraft/aeadditions/commit/1bce0d51b0c560a) M3gaFr3ak *2014-02-03 15:55:31*

**many fixes, certus tank**


[3b0c6089b72b9f7](https://git.the9grounds.com/minecraft/aeadditions/commit/3b0c6089b72b9f7) M3gaFr3ak *2014-02-02 11:18:17*

**texture changes**


[968e3b2b4018c60](https://git.the9grounds.com/minecraft/aeadditions/commit/968e3b2b4018c60) M3gaFr3ak *2014-02-01 18:34:43*

**Added Textures, new parts**


[310c699f14def57](https://git.the9grounds.com/minecraft/aeadditions/commit/310c699f14def57) M3gaFr3ak *2014-02-01 18:31:37*

**First gui work**


[0dc5642699a7768](https://git.the9grounds.com/minecraft/aeadditions/commit/0dc5642699a7768) M3gaFr3ak *2014-01-31 23:35:30*

**update nodes**


[b3635a747055252](https://git.the9grounds.com/minecraft/aeadditions/commit/b3635a747055252) M3gaFr3ak *2014-01-31 20:45:56*

**stuff**


[dc64ed08ed48be0](https://git.the9grounds.com/minecraft/aeadditions/commit/dc64ed08ed48be0) M3gaFr3ak *2014-01-31 20:18:28*

**Updating api**


[a23054dbb4a9fbe](https://git.the9grounds.com/minecraft/aeadditions/commit/a23054dbb4a9fbe) M3gaFr3ak *2014-01-31 07:50:54*

**Initial stuff**


[12b64acfc4cc94f](https://git.the9grounds.com/minecraft/aeadditions/commit/12b64acfc4cc94f) M3gaFr3ak *2014-01-30 19:48:11*

**moved api again**


[a45021ccf37d3f0](https://git.the9grounds.com/minecraft/aeadditions/commit/a45021ccf37d3f0) M3gaFr3ak *2014-01-29 20:40:58*

**moved api**


[467c9deec722aee](https://git.the9grounds.com/minecraft/aeadditions/commit/467c9deec722aee) M3gaFr3ak *2014-01-29 20:37:39*

**meh**


[9419ca502120287](https://git.the9grounds.com/minecraft/aeadditions/commit/9419ca502120287) M3gaFr3ak *2014-01-29 20:31:16*

**Added AE2 API as submodule**


[8c35aa436c8aa0e](https://git.the9grounds.com/minecraft/aeadditions/commit/8c35aa436c8aa0e) M3gaFr3ak *2014-01-29 20:13:05*

**Update to AE2**

 * -EC1 will be continued in its own branch ;)

[1d62999a9bc5dbe](https://git.the9grounds.com/minecraft/aeadditions/commit/1d62999a9bc5dbe) M3gaFr3ak *2014-01-29 20:01:22*

**ExtraCells 1.6.7d**

 * -Fixed Terminal GUI
 * -Fixed Rotations

[cdc524010feabc4](https://git.the9grounds.com/minecraft/aeadditions/commit/cdc524010feabc4) M3gaFr3ak *2014-01-29 15:52:01*

**restored ae13 compatibility -.-'**


[b1e022621028db6](https://git.the9grounds.com/minecraft/aeadditions/commit/b1e022621028db6) M3gaFr3ak *2014-01-26 15:33:06*

**ExtraCells 1.6.7c**

 * -Fixed Importbus being power hungry

[d4e40c88fabaeaa](https://git.the9grounds.com/minecraft/aeadditions/commit/d4e40c88fabaeaa) M3gaFr3ak *2014-01-25 15:55:15*

**travis**


[27df4091442b319](https://git.the9grounds.com/minecraft/aeadditions/commit/27df4091442b319) M3gaFr3ak *2014-01-22 20:07:04*

**ExtraCells 1.6.7b**

 * -partial Logistics Pipes (will become bigger)
 * -Fixed bugs
 * -Level Emitter textbox reacts to keyboard

[fff37fa1edcd764](https://git.the9grounds.com/minecraft/aeadditions/commit/fff37fa1edcd764) M3gaFr3ak *2014-01-22 19:41:44*

**AAAAAAnd it's back :D**


[43f864e9b14bc1a](https://git.the9grounds.com/minecraft/aeadditions/commit/43f864e9b14bc1a) M3gaFr3ak *2014-01-20 16:50:18*

**the interfac will come back, but need to rename it**


[6f022723c5ad9c7](https://git.the9grounds.com/minecraft/aeadditions/commit/6f022723c5ad9c7) M3gaFr3ak *2014-01-20 16:49:53*

**fix**


[1fca013962d9c04](https://git.the9grounds.com/minecraft/aeadditions/commit/1fca013962d9c04) M3gaFr3ak *2014-01-20 16:45:27*

**Added Interface for Network Fluid Interaction**


[c0f78e67dea0096](https://git.the9grounds.com/minecraft/aeadditions/commit/c0f78e67dea0096) M3gaFr3ak *2014-01-20 16:02:33*

**fixed waila**


[7622bcc6b632b9e](https://git.the9grounds.com/minecraft/aeadditions/commit/7622bcc6b632b9e) M3gaFr3ak *2014-01-17 16:04:46*

**fix2**


[d88cd9aeb3849ac](https://git.the9grounds.com/minecraft/aeadditions/commit/d88cd9aeb3849ac) M3gaFr3ak *2014-01-17 15:16:32*

**fix1**


[c72583ea4e8b395](https://git.the9grounds.com/minecraft/aeadditions/commit/c72583ea4e8b395) M3gaFr3ak *2014-01-17 15:16:24*

**fix**


[9581d37b99e6947](https://git.the9grounds.com/minecraft/aeadditions/commit/9581d37b99e6947) M3gaFr3ak *2014-01-17 15:12:14*

**ExtraCells 1.6.7**

 * -Added WAILA Support for Fluid Storage Monitor
 * -Tank rendering done well :D

[c794084e9d384ca](https://git.the9grounds.com/minecraft/aeadditions/commit/c794084e9d384ca) M3gaFr3ak *2014-01-17 15:08:37*

**Overhauls the in world rendering of the Certus Tank.**

 * This solves the massive fps loss from looking at a large bank of Certus Tanks.
 * It also changes how a tower of Certus Tanks look in the work.

[119dc12607b549d](https://git.the9grounds.com/minecraft/aeadditions/commit/119dc12607b549d) delta534 *2014-01-17 08:32:38*

**ExtraCells 1.6.6c**

 * -Battery now woorks like a redstone conrolled version of the energy cell
 * -Added further information in the Fluid Mode Selector

[5a62680d5195307](https://git.the9grounds.com/minecraft/aeadditions/commit/5a62680d5195307) M3gaFr3ak *2014-01-16 16:46:24*

**ExtraCells 1.6.6b**

 * -Fixed Import Bus Power Issue
 * -Performance Fixes on Certus Tank

[e67c1829a9dd75c](https://git.the9grounds.com/minecraft/aeadditions/commit/e67c1829a9dd75c) M3gaFr3ak *2014-01-14 08:27:13*

**This should fix the server/client sync issues for the Certus Tanks.**

 * Performance still needs to be fully tested, though.

[545f983e42cf5a9](https://git.the9grounds.com/minecraft/aeadditions/commit/545f983e42cf5a9) delta534 *2014-01-14 07:40:27*

**unescaped stuff, it shows up correctly after compilation**


[bb1da2b35266eb0](https://git.the9grounds.com/minecraft/aeadditions/commit/bb1da2b35266eb0) M3gaFr3ak *2014-01-13 20:58:20*

**ExtraCells 1.6.6**

 * -Just set the wrong default value for classic bucketr/kb display

[f33c80be3466f65](https://git.the9grounds.com/minecraft/aeadditions/commit/f33c80be3466f65) M3gaFr3ak *2014-01-13 19:40:10*

**ExtraCells 1.6.6**

 * -Added config option for power usage
 * -Added config option for mb/t
 * -Added config option for tickrate
 * -Added config option for kB vs 1000000mB

[c7f1b69c4448651](https://git.the9grounds.com/minecraft/aeadditions/commit/c7f1b69c4448651) M3gaFr3ak *2014-01-13 18:03:41*

**updated readme again**


[860527003d4d01a](https://git.the9grounds.com/minecraft/aeadditions/commit/860527003d4d01a) M3gaFr3ak *2014-01-13 17:08:31*

**Updated readme**


[2e3653b80ec5d2f](https://git.the9grounds.com/minecraft/aeadditions/commit/2e3653b80ec5d2f) M3gaFr3ak *2014-01-13 17:00:16*

**Gradle ain't like '$' in .lang files :D**

 * -escaped the $: &quot;\$&quot;

[4f124aec8d61b85](https://git.the9grounds.com/minecraft/aeadditions/commit/4f124aec8d61b85) M3gaFr3ak *2014-01-13 16:36:10*

**id conflict fixes**


[97d8b22562c2c2d](https://git.the9grounds.com/minecraft/aeadditions/commit/97d8b22562c2c2d) M3gaFr3ak *2014-01-12 19:32:25*

**Fixed language stuff to utf8**


[934e130d9b8a641](https://git.the9grounds.com/minecraft/aeadditions/commit/934e130d9b8a641) M3gaFr3ak *2014-01-12 18:18:30*

**refactor of the language stuff**


[10b4cf36cc708c7](https://git.the9grounds.com/minecraft/aeadditions/commit/10b4cf36cc708c7) M3gaFr3ak *2014-01-12 18:04:55*

**ExtraCells 1.6.5b**

 * -Performance fixes
 * -fixes

[3842fe42f47be57](https://git.the9grounds.com/minecraft/aeadditions/commit/3842fe42f47be57) M3gaFr3ak *2014-01-12 11:13:06*

**versionnumber**


[fd425b968061cb5](https://git.the9grounds.com/minecraft/aeadditions/commit/fd425b968061cb5) M3gaFr3ak *2014-01-10 14:44:46*

**ExtraCells 1.6.5**

 * -Added Recipe for Fluid Storage Monitor
 * -Fixed FSM coloring glitch

[ed4e563cdf0b8e7](https://git.the9grounds.com/minecraft/aeadditions/commit/ed4e563cdf0b8e7) M3gaFr3ak *2014-01-10 14:44:14*

**Fixes**


[c47b9fae2118916](https://git.the9grounds.com/minecraft/aeadditions/commit/c47b9fae2118916) Leon Loeser *2014-01-09 11:51:04*

**ExtraCells 1.6.4**

 * -Fluid Storage Monitor works, no recipe yet
 * -Support for Memory Cards

[9d771cb265805dc](https://git.the9grounds.com/minecraft/aeadditions/commit/9d771cb265805dc) M3gaFr3ak *2014-01-08 20:34:39*

**Legal stuff**

 * License update
 * Render fixes
 * functionality stuffs

[99fa1802409c4b0](https://git.the9grounds.com/minecraft/aeadditions/commit/99fa1802409c4b0) M3gaFr3ak *2014-01-08 14:38:18*

**ExtraCells 1.6.3d**

 * -NPE Fix

[a17300343f64fc1](https://git.the9grounds.com/minecraft/aeadditions/commit/a17300343f64fc1) M3gaFr3ak *2014-01-07 14:34:46*

**Certustank fix**

 * -fixed certus tank clientsyncing with server causing fluid losses

[bd45b41c6ebea85](https://git.the9grounds.com/minecraft/aeadditions/commit/bd45b41c6ebea85) M3gaFr3ak *2014-01-06 14:52:56*

**ExtraCells 1.6.3c**

 * -Fixed AE13 crashbug

[68850f52172fbf1](https://git.the9grounds.com/minecraft/aeadditions/commit/68850f52172fbf1) M3gaFr3ak *2014-01-06 10:09:50*

**ExtraCells 1.6.3b**

 * -Fixed NPE
 * -Fixed AOOBE
 * -CertusTank improvements
 * -Bus redstone issue fix

[f2603941140d94f](https://git.the9grounds.com/minecraft/aeadditions/commit/f2603941140d94f) M3gaFr3ak *2014-01-05 19:35:18*

**Sync**

 * -Replaced Certus Tank descriptive packetswith block events

[d5d9bcd55922b44](https://git.the9grounds.com/minecraft/aeadditions/commit/d5d9bcd55922b44) Leon Loeser *2014-01-05 15:07:10*

**ExtraCells 1.6.3**

 * -Fluid Terminal now saves its selected Fluid in nbt
 * -Fluid Terminal is now an ISidedInventory, so you can use it as a
 * FluidTransposer :D

[212cbfb6d76f40b](https://git.the9grounds.com/minecraft/aeadditions/commit/212cbfb6d76f40b) Leon Loeser *2014-01-04 09:49:04*

**Perforance Fixes**

 * -Buses now Buffer the adjacent IFluidHandler
 * -Removed unnecessary casts

[278931485bcaefd](https://git.the9grounds.com/minecraft/aeadditions/commit/278931485bcaefd) Leon Loeser *2014-01-03 21:41:21*

**Fixes**

 * -Fixed custom/costum typo
 * -Some cleanup
 * -Fixed possible NPE on interface

[38e43e15b8ba76c](https://git.the9grounds.com/minecraft/aeadditions/commit/38e43e15b8ba76c) Leon Loeser *2014-01-02 21:20:16*

**ExtraCells 1.6.2d**

 * -Fixed NPE

[129ca9a083b2021](https://git.the9grounds.com/minecraft/aeadditions/commit/129ca9a083b2021) Leon Loeser *2014-12-30 19:58:06*

**ExtraCells 1.6.2c**

 * -Fixed a crashbug

[9a2b4b1f510f2cd](https://git.the9grounds.com/minecraft/aeadditions/commit/9a2b4b1f510f2cd) Leon Loeser *2014-12-29 18:38:58*

**ExtraCells 1.6.2b**

 * -Performance improvements on export, import bus
 * -Recipe bugbix for adjustable storage
 * -First prototype of ME Fluid Storage Monitor (not craftable)

[9b47f8ad73ac72f](https://git.the9grounds.com/minecraft/aeadditions/commit/9b47f8ad73ac72f) M3gaFr3ak *2013-12-28 11:44:27*

**ExtraCells 1.6.2**

 * -Fixed NPE in Fluid Terminal

[ed523767b7f423b](https://git.the9grounds.com/minecraft/aeadditions/commit/ed523767b7f423b) M3gaFr3ak *2013-12-25 21:22:07*

**ExtraCells 1.6.1d**

 * -Fixed Fluid Terminal GUI rendering tooltips UNDER text ôO

[bc3001dcb8e7768](https://git.the9grounds.com/minecraft/aeadditions/commit/bc3001dcb8e7768) M3gaFr3ak *2013-12-23 17:31:34*

**Merge branch 'master' of https://github.com/M3gaFr3ak/ExtraCells**


[11494c53d3d6e02](https://git.the9grounds.com/minecraft/aeadditions/commit/11494c53d3d6e02) M3gaFr3ak *2013-12-23 17:19:22*

**Changed from GMCP to ForgeGradle**

 * Fatality! Forge wins (against modloader support :D)

[34b2f9fe5bdcec2](https://git.the9grounds.com/minecraft/aeadditions/commit/34b2f9fe5bdcec2) M3gaFr3ak *2013-12-23 17:18:45*

**ExtraCells 1.6.1c**

 * -Fixed ArrayOutOfBoundsException on the storagebus
 * -Fixed crash when trying to render fluids w/o texture in certusTank

[98e2b5ee5974a84](https://git.the9grounds.com/minecraft/aeadditions/commit/98e2b5ee5974a84) Leon Loeser *2013-12-22 10:19:33*

**ExtraCells 1.6.1b**

 * -NPE Fix

[362e8a224d4e50a](https://git.the9grounds.com/minecraft/aeadditions/commit/362e8a224d4e50a) M3gaFr3ak *2013-12-19 15:42:45*

**ExtraCells 1.6.1**

 * STUPID VERSION NUMBER

[2d6fd45a8e3e590](https://git.the9grounds.com/minecraft/aeadditions/commit/2d6fd45a8e3e590) M3gaFr3ak *2013-12-17 18:26:17*

**ExtraCells 1.6.1**

 * forgot version number

[50967da1b0e1e78](https://git.the9grounds.com/minecraft/aeadditions/commit/50967da1b0e1e78) M3gaFr3ak *2013-12-17 18:19:56*

**Quick cleanup**

 * -Removed unused imports
 * -Re-enabled warnings
 * -Minecraft always uses raw List. Notch is annoying

[2d2c244e60ea203](https://git.the9grounds.com/minecraft/aeadditions/commit/2d2c244e60ea203) Leon Loeser *2013-12-17 14:27:18*

**ExtraCells 1.6.0d**

 * -Frgot the stupid version number

[85f6a9823f7d7c8](https://git.the9grounds.com/minecraft/aeadditions/commit/85f6a9823f7d7c8) Leon Loeser *2013-12-17 11:37:29*

**ExtraCells 1.6.0d**

 * -Fixed NPE on FluidCrafter
 * -Made FluidCrafter AE14-only (just wnt register in AE13)
 * -Fixed ID Mapping of FluidVoid

[24aa2e9ed3c0946](https://git.the9grounds.com/minecraft/aeadditions/commit/24aa2e9ed3c0946) Leon Loeser *2013-12-17 11:32:30*

**ExtraCells 1.6.1**

 * -Fixed Bug in FluidCrafter Inventory
 * -Added check if recipe exists ôO
 * -Bugfix

[0d2ad739d477458](https://git.the9grounds.com/minecraft/aeadditions/commit/0d2ad739d477458) M3gaFr3ak *2013-12-16 17:59:10*

**ExtraCells 1.6.0c**

 * -Added ID Mismatch detector

[bbfcf79d30351a5](https://git.the9grounds.com/minecraft/aeadditions/commit/bbfcf79d30351a5) M3gaFr3ak *2013-12-15 20:32:25*

**ExtraCells 1.6.0b**

 * -Fixed Fluid Interface crash

[387cab97eeee521](https://git.the9grounds.com/minecraft/aeadditions/commit/387cab97eeee521) M3gaFr3ak *2013-12-15 12:11:44*

**ExtraCells 1.6.0**

 * -Fixed blockupdate on levelemitter
 * -Other bugfixes
 * -New block: ME Fluid Crafting Chamber, crafts Recipes which contains
 * filled fluidContainers with pure fluid

[421c4e2425d1630](https://git.the9grounds.com/minecraft/aeadditions/commit/421c4e2425d1630) M3gaFr3ak *2013-12-15 11:44:01*

**ExtraCells 1.5.9c**

 * -Tiny fix on certus tank
 * -first experiment with fluid crafting, not ingame yet, but greatness
 * awaits (or not)

[e90c7c8e68487c0](https://git.the9grounds.com/minecraft/aeadditions/commit/e90c7c8e68487c0) M3gaFr3ak *2013-12-11 16:05:05*

**Update zh_CN.xml**


[289a1b9b611dceb](https://git.the9grounds.com/minecraft/aeadditions/commit/289a1b9b611dceb) SSCXM *2013-12-10 10:58:55*

**ExtraCells 1.5.9b**

 * -Fixed rendering glitch on certus tank
 * -fixed certus tank sending too many packets
 * -Fixed storage bus sending too many packets

[2ac4e3a59b4aabe](https://git.the9grounds.com/minecraft/aeadditions/commit/2ac4e3a59b4aabe) M3gaFr3ak *2013-12-09 17:37:10*

**Update de_DE.xml**


[387c924bd8bbc07](https://git.the9grounds.com/minecraft/aeadditions/commit/387c924bd8bbc07) Vexatos *2013-12-09 16:58:47*

**ExtraCells 1.5.9**

 * -New Block: Fluid Void
 * -Fluid Interface now needs power...
 * -Fixed some other stuff

[b087708e72cd0e2](https://git.the9grounds.com/minecraft/aeadditions/commit/b087708e72cd0e2) M3gaFr3ak *2013-12-07 18:36:50*

**ExtraCells 1.5.8c**

 * -Fixed NPE

[be9eb9ce8ed030b](https://git.the9grounds.com/minecraft/aeadditions/commit/be9eb9ce8ed030b) M3gaFr3ak *2013-12-07 15:56:15*

**ExtraCells 1.5.8b**

 * -Forgot stupid version number

[822d299ec59aaf1](https://git.the9grounds.com/minecraft/aeadditions/commit/822d299ec59aaf1) M3gaFr3ak *2013-12-06 16:47:03*

**ExtraCells 1.5.8b**

 * -Fixed NPE
 * -Fixed ID problems

[7edddd8899d1400](https://git.the9grounds.com/minecraft/aeadditions/commit/7edddd8899d1400) M3gaFr3ak *2013-12-06 16:44:56*

**ExtraCells 1.5.8**

 * -Fixed some Certus Tank rendering
 * -Reduced amount of packets being sent by a 25th
 * -Fixed NPE on battery
 * -Fixed NPE on solderingstation
 * -fixed ae/t imbalance on import bus
 * -fixed me drive accepting wrong items
 * -fixed more npes
 * -added me fluid interface

[0a2bf18a650c6b4](https://git.the9grounds.com/minecraft/aeadditions/commit/0a2bf18a650c6b4) M3gaFr3ak *2013-11-30 23:45:39*

**ExtraCells 1.5.7c**

 * -STUPID VERSION NUMBER!!!!!

[ea46242ff8f5004](https://git.the9grounds.com/minecraft/aeadditions/commit/ea46242ff8f5004) M3gaFr3ak *2013-11-29 13:32:03*

**ExtraCells 1.5.7c**

 * -Fixed ME Drive accepting all items
 * -Added try/catch for ID conflict NPE&#x27;s
 * -Code Optimization

[35b71202b0a76c0](https://git.the9grounds.com/minecraft/aeadditions/commit/35b71202b0a76c0) M3gaFr3ak *2013-11-29 13:29:58*

**ExtraCells 1.5.7b**

 * -Fixed some packet-performance issues on CertusTanks and storage bus
 * -Fixed ME Backup Battery

[87542b931c371c6](https://git.the9grounds.com/minecraft/aeadditions/commit/87542b931c371c6) M3gaFr3ak *2013-11-28 15:55:08*

**Fixing bug with storingPower**

 * Renamed the variable to make semantics match usage.
 * Caching the state of storingPower and updating on block update.
 * Fixing logic to correctly return power to the network without
 * overflowing the battery if the network has a surplus.

[fc8ca631d51b6e3](https://git.the9grounds.com/minecraft/aeadditions/commit/fc8ca631d51b6e3) Matthew Walker *2013-11-28 04:37:27*

**ExtraCells 1.5.7**

 * -Fixed GUI bugs
 * -Made certus tank work like BC tank
 * -Optimizations
 * -Fixed NPE

[6dfcb4f3a8e85e7](https://git.the9grounds.com/minecraft/aeadditions/commit/6dfcb4f3a8e85e7) M3gaFr3ak *2013-11-27 20:15:37*

**ExtraCells 1.5.6d**

 * -Fixed dupe bug

[6b9d3658911d7f2](https://git.the9grounds.com/minecraft/aeadditions/commit/6b9d3658911d7f2) Leon Loeser *2013-11-23 20:51:58*

**ExtraCells 1.5.6c**

 * -Fixed NPE on SolderingStation GUI
 * -Fixed NPE on Fluid Storage Bus

[281376f23007cc7](https://git.the9grounds.com/minecraft/aeadditions/commit/281376f23007cc7) Leon Loeser *2013-11-22 12:27:56*

**ExtraCells 1.5.6b**

 * -forgot version number

[879ddb8f9259006](https://git.the9grounds.com/minecraft/aeadditions/commit/879ddb8f9259006) Leon Loeser *2013-11-21 06:06:06*

**ExtraCells 1.5.6b**

 * -fixed NPE

[2be3a258f7088c7](https://git.the9grounds.com/minecraft/aeadditions/commit/2be3a258f7088c7) Leon Loeser *2013-11-20 16:19:03*

**ExtraCells 1.5.6**

 * -Major bugfixes in the fluid terminal gui
 * -fixed NPE in fluid storage bus
 * -totally new renderer for fluid terminal
 * -fluid terminal has coloring options
 * -fluid terminal glows in the dark if active

[fd2ba1a35d65d61](https://git.the9grounds.com/minecraft/aeadditions/commit/fd2ba1a35d65d61) Leon Loeser *2013-11-19 17:29:40*

**ExtraCells 1.5.5e**

 * -Fixed IndexOutOfRange Exception
 * -Fixed storages needing a second storage to accept more than 1 type of
 * fluids

[0040db8827b49e4](https://git.the9grounds.com/minecraft/aeadditions/commit/0040db8827b49e4) Leon Loeser *2013-11-16 17:39:15*

**ExtraCells 1.5.5d**

 * -Fixed NPE in terminal
 * -Fixed NPE in transition plane

[2ee31ff2526f6cd](https://git.the9grounds.com/minecraft/aeadditions/commit/2ee31ff2526f6cd) Leon Loeser *2013-11-15 18:18:36*

**ExtraCekks 1.5.5c**

 * -Fixed crash on cellcasings
 * -Fixed colorcrash

[e795c29880044e8](https://git.the9grounds.com/minecraft/aeadditions/commit/e795c29880044e8) Leon Loeser *2013-11-14 17:27:46*

**Update de_DE.xml**


[fb2cd4c10d040e0](https://git.the9grounds.com/minecraft/aeadditions/commit/fb2cd4c10d040e0) Vexatos *2013-11-13 18:04:05*

**Merge branch 'master' of https://github.com/M3gaFr3ak/ExtraCells.git**


[91e2aa640d29e59](https://git.the9grounds.com/minecraft/aeadditions/commit/91e2aa640d29e59)  *2013-11-13 16:39:18*

**ExtraCells 1.5.5b**


[2c335bb430aca2b](https://git.the9grounds.com/minecraft/aeadditions/commit/2c335bb430aca2b) M3gaFr3ak *2013-11-13 16:36:54*

**Merge branch 'master' of https://github.com/M3gaFr3ak/ExtraCells**


[6b6a43d87cbad6e](https://git.the9grounds.com/minecraft/aeadditions/commit/6b6a43d87cbad6e) Leon Loeser *2013-11-13 15:25:32*

**ExtraCells 1.5.5b**

 * -Added searchbar functionality to fluid terminal

[b8257005fd5d755](https://git.the9grounds.com/minecraft/aeadditions/commit/b8257005fd5d755) Leon Loeser *2013-11-13 15:25:20*

**ExtraCells 1.5.5**


[717067657c884c7](https://git.the9grounds.com/minecraft/aeadditions/commit/717067657c884c7)  *2013-11-13 07:09:58*

**ExtraCells 1.5.5**

 * -New FrontTexture for the FluidTerminal
 * -New Gui for the FluidTerminal

[e833d49ef9d4e7e](https://git.the9grounds.com/minecraft/aeadditions/commit/e833d49ef9d4e7e) Leon Loeser *2013-11-12 21:06:38*

**ExtraCells 1.5.4e**

 * -Fixed NPE on FluidStorages

[8928cba71111aaa](https://git.the9grounds.com/minecraft/aeadditions/commit/8928cba71111aaa) Leon Loeser *2013-11-12 14:45:51*

**ExtraCells 1.5.4d**

 * -Recoded SolderingStation, so it WORKS!
 * -Recoded Adjustable ME Storage, so it WORKS!

[fb3ff7e57ea4edd](https://git.the9grounds.com/minecraft/aeadditions/commit/fb3ff7e57ea4edd) Leon Loeser *2013-11-11 18:20:29*

**ExtraCells 1.5.4c**

 * -WHY DO I ALWAYS FORGET THE VERSION NUMBER?!

[19fe4c635490419](https://git.the9grounds.com/minecraft/aeadditions/commit/19fe4c635490419) Leon Loeser *2013-11-11 16:16:56*

**ExtraCells 1.5.4c**

 * -Fixed crash in ImportBusGui when clicking the fluidmode button
 * -Changed fluid storage to save fluid with it NAME, not with its ID
 * -Added temporary code to restore fluids saved by ID (before this
 * update), will get removed 5 major versions later or so.

[1d3b59449e4be85](https://git.the9grounds.com/minecraft/aeadditions/commit/1d3b59449e4be85) Leon Loeser *2013-11-11 16:16:00*

**ExtraCells 1.5.4b**

 * -Fixed version number in build.gradle

[169b6bb0668b517](https://git.the9grounds.com/minecraft/aeadditions/commit/169b6bb0668b517) Leon Loeser *2013-11-11 14:21:11*

**ExtraCells 1.5.4b**

 * -Added german localization for the fluid transport modes
 * -Fixed english localizazion of fluid transport modes

[8645d37433d26cb](https://git.the9grounds.com/minecraft/aeadditions/commit/8645d37433d26cb) Leon Loeser *2013-11-11 14:20:14*

**ExtraCells 1.5.4b**

 * -Added tooltips for the Mode Buttons in the GUIs of Import and Export
 * bus

[bc1ce29bc53d853](https://git.the9grounds.com/minecraft/aeadditions/commit/bc1ce29bc53d853) Leon Loeser *2013-11-11 14:16:18*

**ExtraCells 1.5.4**

 * -Localization for Water and Lava for the FluidAPI (Yep, the fluid api
 * forgot to add a water/lava localization)
 * -Updated German localization for Lava and Water
 * -Fixed coordinates of Redstone Mode Button in GUI of Export and Import
 * bus
 * -Added button to select the ex/import speed for the export and import
 * bus and added different costs.

[b0c5d797748da1e](https://git.the9grounds.com/minecraft/aeadditions/commit/b0c5d797748da1e) Leon Loeser *2013-11-11 13:16:26*

**Update de_DE.xml**


[03d71d1d7be5cc7](https://git.the9grounds.com/minecraft/aeadditions/commit/03d71d1d7be5cc7) Vexatos *2013-11-10 19:22:15*

**ExtraCels 1.5.3b**

 * -Forgot to edit version number

[3235a3c44cfd8c4](https://git.the9grounds.com/minecraft/aeadditions/commit/3235a3c44cfd8c4) Leon Loeser *2013-11-10 18:32:50*

**ExtraCells 1.5.3b**

 * -Added caching of preformatted fluids
 * -Added warning message for certain id overrides

[a55d4dc68b165c1](https://git.the9grounds.com/minecraft/aeadditions/commit/a55d4dc68b165c1) Leon Loeser *2013-11-10 18:30:35*

**ExtraCells 1.5.3**

 * -Fixed NPE sometimes being caused by other mods
 * -Performance improvement on fluid storage, caches inventory. adding
 * preformattation caching soon
 * -Added scrollwheel functionality to the fluid terminal
 * -Fixed y-fighting on certus tank renderer
 * -Added version.properties

[574731b2c4d602d](https://git.the9grounds.com/minecraft/aeadditions/commit/574731b2c4d602d) Leon Loeser *2013-11-09 21:44:19*

**ExtraCells 1.5.2c**

 * -Fixed NPE&#x27;s

[d38b9221c768859](https://git.the9grounds.com/minecraft/aeadditions/commit/d38b9221c768859) Leon Loeser *2013-11-08 17:25:03*

**ExtraCells 1.5.2b**

 * -Fixed crash on shiftrightclicking fluid storage

[d5f4967911826c7](https://git.the9grounds.com/minecraft/aeadditions/commit/d5f4967911826c7) Leon Loeser *2013-11-08 15:04:26*

**ExtraCells 1.5.2**

 * -Added ME Fluid Level Emitter
 * -Fixed some rendering coordinates

[6a387c8309cb124](https://git.the9grounds.com/minecraft/aeadditions/commit/6a387c8309cb124) Leon Loeser *2013-11-07 17:33:27*

**ExtraCells 1.5.1d**

 * -Fixed severe bug!

[13cf13b90b97ce4](https://git.the9grounds.com/minecraft/aeadditions/commit/13cf13b90b97ce4) Leon Loeser *2013-11-05 16:02:31*

**updated zh_CN.xml for ExtraCells 1.5.1**


[780a8c0249803d3](https://git.the9grounds.com/minecraft/aeadditions/commit/780a8c0249803d3) crafteverywhere *2013-11-04 01:50:19*

**ExtraCells 1.5.1c**

 * -forgot to change version number :/

[bdc70f6086799d7](https://git.the9grounds.com/minecraft/aeadditions/commit/bdc70f6086799d7) Leon Loeser *2013-11-03 20:06:02*

**ExtraCells 1.5.1c**

 * -Fixed FluidDisplayItem being in normal storage cells

[590863f5fee43e2](https://git.the9grounds.com/minecraft/aeadditions/commit/590863f5fee43e2) Leon Loeser *2013-11-03 20:02:32*

**Update de_DE.xml**


[45a07e71f2e6a27](https://git.the9grounds.com/minecraft/aeadditions/commit/45a07e71f2e6a27) Vexatos *2013-11-03 18:12:20*

**ExtraCells 1.5.1b**

 * -Fixed crashbug on Fluid-Storage-Bus

[e155c27a103d68a](https://git.the9grounds.com/minecraft/aeadditions/commit/e155c27a103d68a) M3gaFr3ak *2013-11-01 20:53:12*

**ExtraCells 1.5.1**

 * -Compatibility for AE 13 and 14
 * -Fixed IO Port crash
 * -Java 6 compatibility is back :D
 * -fixed coloring
 * -supply bus beta, not craftable yet, gotta get a fixed inventory

[6fabb31aaf739da](https://git.the9grounds.com/minecraft/aeadditions/commit/6fabb31aaf739da) Leon Loeser *2013-10-30 18:45:13*

**Update zh_CN.xml**


[af1ad969b66ede1](https://git.the9grounds.com/minecraft/aeadditions/commit/af1ad969b66ede1) crafteverywhere *2013-10-27 03:17:25*

**Added build Status**


[63a0e58bb84a521](https://git.the9grounds.com/minecraft/aeadditions/commit/63a0e58bb84a521) Leon Loeser *2013-10-22 18:40:41*

**Added a ton of hyphens (It is German, remember)**


[080100c9f5df809](https://git.the9grounds.com/minecraft/aeadditions/commit/080100c9f5df809) Vexatos *2013-10-22 15:27:07*

**new recipes, colors, stuff? :D**


[51f3162e8e765f8](https://git.the9grounds.com/minecraft/aeadditions/commit/51f3162e8e765f8) Leon Loeser *2013-10-22 13:56:10*

**changed gitignore**


[a372bca3a6de921](https://git.the9grounds.com/minecraft/aeadditions/commit/a372bca3a6de921) Leon Loeser *2013-10-19 18:32:59*

**added api...**


[66de47268a3a0ac](https://git.the9grounds.com/minecraft/aeadditions/commit/66de47268a3a0ac) Leon Loeser *2013-10-19 18:21:30*

**WHAT DO YOU EXPECT?!**


[19fd27b782cba69](https://git.the9grounds.com/minecraft/aeadditions/commit/19fd27b782cba69) M3gaFr3ak *2013-10-18 22:17:40*

**"refactoring" the buildsystem**

 * moved to gradle and gMCP

[c140458b3d6ac7f](https://git.the9grounds.com/minecraft/aeadditions/commit/c140458b3d6ac7f) Leon Loeser *2013-10-18 22:15:29*

**redid stuff**


[cfc5cdb2188c3f1](https://git.the9grounds.com/minecraft/aeadditions/commit/cfc5cdb2188c3f1) Leon Loeser *2013-10-18 20:56:48*

**other stuff for jenkins**


[7227f9c676efca4](https://git.the9grounds.com/minecraft/aeadditions/commit/7227f9c676efca4) Leon Loeser *2013-10-18 20:52:22*

**renamed bluid_obfed to build because of jenkins stuffs :F**


[f9417ef753e3526](https://git.the9grounds.com/minecraft/aeadditions/commit/f9417ef753e3526) Leon Loeser *2013-10-18 20:43:47*

**remade the cleaup stuff in ant**


[1843ae2e4e05dec](https://git.the9grounds.com/minecraft/aeadditions/commit/1843ae2e4e05dec) Leon Loeser *2013-10-18 20:35:51*

**removed shit**


[ad2a08c5baaf867](https://git.the9grounds.com/minecraft/aeadditions/commit/ad2a08c5baaf867) Leon Loeser *2013-10-18 20:19:51*

**removed sign jar stuff**


[ba8eee45c4c8918](https://git.the9grounds.com/minecraft/aeadditions/commit/ba8eee45c4c8918) Leon Loeser *2013-10-18 20:16:53*

**colorable buses.**


[7f652a530ac93b2](https://git.the9grounds.com/minecraft/aeadditions/commit/7f652a530ac93b2) Leon Loeser *2013-10-17 12:55:38*

**Dupebugfix, updatefix, eventbased, ENUM**


[e0b140e56949338](https://git.the9grounds.com/minecraft/aeadditions/commit/e0b140e56949338) Leon Loeser *2013-10-12 22:26:10*

**nbt fixes, graphic fixes... :D**


[9fd2a1ad5b5ef23](https://git.the9grounds.com/minecraft/aeadditions/commit/9fd2a1ad5b5ef23) Leon Loeser *2013-10-10 16:37:43*

**Rewrite of Import/export-bus functionality!**


[35846ba8c5dfa8d](https://git.the9grounds.com/minecraft/aeadditions/commit/35846ba8c5dfa8d) Leon Loeser *2013-10-06 18:39:20*

**idk**


[41c57da4bbbaa19](https://git.the9grounds.com/minecraft/aeadditions/commit/41c57da4bbbaa19) Leon Loeser *2013-10-06 12:58:15*

**StorageBus Priority, Tank Item rendering**


[03a0959f217089e](https://git.the9grounds.com/minecraft/aeadditions/commit/03a0959f217089e) Leon Loeser *2013-10-05 22:21:15*

**new feature: register tileentities for spatial io ports :D**


[40c4ee1d9c2b4b0](https://git.the9grounds.com/minecraft/aeadditions/commit/40c4ee1d9c2b4b0) Leon Loeser *2013-10-04 19:49:49*

**added proper packethandler**


[d97e9768f7155fd](https://git.the9grounds.com/minecraft/aeadditions/commit/d97e9768f7155fd) Leon Loeser *2013-10-04 18:30:17*

**Fixed bugs, redstonemode for buses---**


[3b978f6e8303461](https://git.the9grounds.com/minecraft/aeadditions/commit/3b978f6e8303461) Leon Loeser *2013-10-03 21:17:40*

**update to latest forge:P**

 * and all the other mods i use to debug are not updated -.-.-

[8b733601554b5bb](https://git.the9grounds.com/minecraft/aeadditions/commit/8b733601554b5bb) Leon Loeser *2013-09-26 17:48:35*

**drain tank wth containers**


[c646e5102f7ba27](https://git.the9grounds.com/minecraft/aeadditions/commit/c646e5102f7ba27) Leon Loeser *2013-09-24 15:37:49*

**Tank functionality**


[b6a7e11dfc64dd4](https://git.the9grounds.com/minecraft/aeadditions/commit/b6a7e11dfc64dd4) Leon Loeser *2013-09-23 19:55:40*

**Update de_DE.xml**


[1aa366a0665082f](https://git.the9grounds.com/minecraft/aeadditions/commit/1aa366a0665082f) Vexatos *2013-09-23 19:54:53*

**fixed bugs, added tank model**


[1952ec508524e3b](https://git.the9grounds.com/minecraft/aeadditions/commit/1952ec508524e3b) Leon Loeser *2013-09-23 16:09:01*

**Tons of BugFixes :D**


[1509eb4d55caea0](https://git.the9grounds.com/minecraft/aeadditions/commit/1509eb4d55caea0) Leon Loeser *2013-09-19 17:05:19*

**Update ru_RU.xml**


[6e38775512b5207](https://git.the9grounds.com/minecraft/aeadditions/commit/6e38775512b5207) Anton *2013-09-15 10:52:56*

**3d models :D**


[0b7981bdb25c5bf](https://git.the9grounds.com/minecraft/aeadditions/commit/0b7981bdb25c5bf) Leon Loeser *2013-09-13 14:49:03*

**fixed dupe bug...**


[2de50c71bd80e81](https://git.the9grounds.com/minecraft/aeadditions/commit/2de50c71bd80e81) Leon Loeser *2013-09-09 21:23:46*

**Update de_DE.xml**

 * &quot;Membran&quot; :(

[a75d0d853328b79](https://git.the9grounds.com/minecraft/aeadditions/commit/a75d0d853328b79) Vexatos *2013-09-04 14:56:51*

**fixes**


[01fceb35fdfb6fb](https://git.the9grounds.com/minecraft/aeadditions/commit/01fceb35fdfb6fb) Leon Loeser *2013-09-03 16:54:07*

**Merge branch 'master' of https://github.com/M3gaFr3ak/ExtraCells**

 * Conflicts:
 * ec_common/extracells/localization/Localization.java
 * cake

[18dd191785483ab](https://git.the9grounds.com/minecraft/aeadditions/commit/18dd191785483ab) Leon Loeser *2013-09-03 16:51:12*

**localization issues**


[f13f5a242c8128b](https://git.the9grounds.com/minecraft/aeadditions/commit/f13f5a242c8128b) Leon Loeser *2013-09-02 19:08:03*

**The name of Chinese language file should be zh_CN**

 * Test the new language file and find a problem,so fix it.

[2979650b4e3e273](https://git.the9grounds.com/minecraft/aeadditions/commit/2979650b4e3e273) crafteverywhere *2013-09-02 18:56:57*

**Update zh_CN.xml**


[893f803f0e84c33](https://git.the9grounds.com/minecraft/aeadditions/commit/893f803f0e84c33) crafteverywhere *2013-09-02 18:26:34*

**removed a redundant call to grid.getCellArray()**

 * &lt;not tested&gt;
 * I saw this when i was looking over your code to learn how to interact with appeng.

[ca67e58f228d46c](https://git.the9grounds.com/minecraft/aeadditions/commit/ca67e58f228d46c) Chris *2013-09-02 02:09:25*

**fixed battery gui (packets)**


[0a7643ea67d66d9](https://git.the9grounds.com/minecraft/aeadditions/commit/0a7643ea67d66d9) Leon Loeser *2013-09-01 21:17:38*

**crashfix, me fluid transition plane**


[69337e6469bc6be](https://git.the9grounds.com/minecraft/aeadditions/commit/69337e6469bc6be) Leon Loeser *2013-09-01 12:39:00*

**fixed bug**


[96fa823c4d0553b](https://git.the9grounds.com/minecraft/aeadditions/commit/96fa823c4d0553b) Leon Loeser *2013-08-28 16:30:59*

**Update Localization.java**


[7e4fd2dce7b7bd7](https://git.the9grounds.com/minecraft/aeadditions/commit/7e4fd2dce7b7bd7) Anton *2013-08-28 16:24:55*

**Russian localization**


[e4d76e21580a9f3](https://git.the9grounds.com/minecraft/aeadditions/commit/e4d76e21580a9f3) Anton *2013-08-28 16:22:32*

**bug fixes**


[783435bff0013f9](https://git.the9grounds.com/minecraft/aeadditions/commit/783435bff0013f9) Leon Loeser *2013-08-24 15:20:24*

**edit**


[e62e770b1ceba1a](https://git.the9grounds.com/minecraft/aeadditions/commit/e62e770b1ceba1a) Leon Loeser *2013-08-22 20:35:25*

**adding gui to me battery**


[8af4f936f3a7fd5](https://git.the9grounds.com/minecraft/aeadditions/commit/8af4f936f3a7fd5) Leon Loeser *2013-08-22 19:48:10*

**Fixed Pipes connecting, fixed take phantomslots, fixed delete items**

 * Took my IInventory out of buses, moved to own class,
 * etc

[7fecb28d2393f7c](https://git.the9grounds.com/minecraft/aeadditions/commit/7fecb28d2393f7c) Leon Loeser *2013-08-20 19:30:18*

**changd coords of slots**


[498897a1b901209](https://git.the9grounds.com/minecraft/aeadditions/commit/498897a1b901209) Leon Loeser *2013-08-18 20:29:45*

**Update de_DE.xml**

 * Now better.

[f16b05e992654f6](https://git.the9grounds.com/minecraft/aeadditions/commit/f16b05e992654f6) Vexatos *2013-08-18 20:27:06*

**Update de_DE.xml**

 * EXPORT &lt;3

[d6c3b81e06ec5e8](https://git.the9grounds.com/minecraft/aeadditions/commit/d6c3b81e06ec5e8) Vexatos *2013-08-18 20:22:01*

**Added export bus; added filters; added recipes**


[24be553e43bed8b](https://git.the9grounds.com/minecraft/aeadditions/commit/24be553e43bed8b) Leon Loeser *2013-08-18 19:58:08*

**more refactoring, started adding priority**


[dfaa8ef6cadb982](https://git.the9grounds.com/minecraft/aeadditions/commit/dfaa8ef6cadb982) Leon Loeser *2013-08-16 21:27:21*

**Refactoring, new Fluid system**


[326e0fa20f48597](https://git.the9grounds.com/minecraft/aeadditions/commit/326e0fa20f48597) Leon Loeser *2013-08-16 16:04:05*

**Refactoring etc, fluidstorage**


[9d04aa693a31ca9](https://git.the9grounds.com/minecraft/aeadditions/commit/9d04aa693a31ca9) Leon Loeser *2013-08-14 15:32:50*

**Fixed bug with duplicate tanks; added rc tank support**


[183312d087ce7e8](https://git.the9grounds.com/minecraft/aeadditions/commit/183312d087ce7e8) Leon Loeser *2013-08-14 13:15:27*

**Create zh_CN.xml**


[484159fa46dfe43](https://git.the9grounds.com/minecraft/aeadditions/commit/484159fa46dfe43) crafteverywhere *2013-08-12 07:23:11*

**Other Fixes, cosmetic things**


[185172087122286](https://git.the9grounds.com/minecraft/aeadditions/commit/185172087122286) Leon Loeser *2013-08-11 17:17:17*

**Update de_DE.xml**

 * Update!

[a0cc76e21321f69](https://git.the9grounds.com/minecraft/aeadditions/commit/a0cc76e21321f69) Vexatos *2013-08-11 16:45:19*

**Localization fixes**


[48cebf8e7fd36f2](https://git.the9grounds.com/minecraft/aeadditions/commit/48cebf8e7fd36f2) Leon Loeser *2013-08-11 16:38:04*

**Update de_DE.xml**

 * Fixed language commit problems.

[66aa2520db7bb20](https://git.the9grounds.com/minecraft/aeadditions/commit/66aa2520db7bb20) Vexatos *2013-08-11 16:29:12*

**Added real Fluid Terminal**


[2acb601bbb11e24](https://git.the9grounds.com/minecraft/aeadditions/commit/2acb601bbb11e24) Leon Loeser *2013-08-11 12:45:56*

**Edit**


[daae77448685548](https://git.the9grounds.com/minecraft/aeadditions/commit/daae77448685548) Leon Loeser *2013-08-11 12:45:36*

**Merge branch 'master' of https://github.com/M3gaFr3ak/ExtraCells**

 * Conflicts:
 * ec_resources/assets/extracells/lang/de_DE.xml

[f31ef2881f7d87d](https://git.the9grounds.com/minecraft/aeadditions/commit/f31ef2881f7d87d) Leon Loeser *2013-08-11 12:41:00*

**Added Fluid Terminal**


[bd433e7a6f9af54](https://git.the9grounds.com/minecraft/aeadditions/commit/bd433e7a6f9af54) Leon Loeser *2013-08-11 12:36:43*

**Update de_DE.xml**

 * Sounds better this way.

[b117585f8e3fa93](https://git.the9grounds.com/minecraft/aeadditions/commit/b117585f8e3fa93) Vexatos *2013-08-05 17:25:54*

**Fixed stuff with etadata to direction**


[da640c4aa3323e6](https://git.the9grounds.com/minecraft/aeadditions/commit/da640c4aa3323e6) Leon Loeser *2013-08-04 21:21:46*

**Added fluid import, storage busses**


[c58097a40377b04](https://git.the9grounds.com/minecraft/aeadditions/commit/c58097a40377b04) Leon Loeser *2013-08-04 20:59:59*

**Corrected entry in ger lng file, Added support**


[a99833c6353356b](https://git.the9grounds.com/minecraft/aeadditions/commit/a99833c6353356b) Leon Loeser *2013-08-03 07:42:01*

**Update en_US.xml**

 * Correct capitalizing.

[8d62dc352ae6aac](https://git.the9grounds.com/minecraft/aeadditions/commit/8d62dc352ae6aac) Vexatos *2013-08-03 06:45:18*

**Refactor, Localization Support**


[2fb5b78abb4ca1d](https://git.the9grounds.com/minecraft/aeadditions/commit/2fb5b78abb4ca1d) Leon Loeser *2013-08-02 22:36:38*

**Added Blast resistant ME Drive and fixed rotation of Soldering Station**


[3f41a9af17e2dc4](https://git.the9grounds.com/minecraft/aeadditions/commit/3f41a9af17e2dc4) Leon Loeser *2013-08-02 06:48:05*

**WIP Block, ME Backup Battery**


[abeee1b85c8846b](https://git.the9grounds.com/minecraft/aeadditions/commit/abeee1b85c8846b) Leon Loeser *2013-07-29 12:44:36*

**Fixed GUI texture**


[98b118308257eac](https://git.the9grounds.com/minecraft/aeadditions/commit/98b118308257eac) Leon Loeser *2013-07-28 21:36:26*

**Fixed Texture Probem**

 * removed / xxd

[5a7158a0af74a0d](https://git.the9grounds.com/minecraft/aeadditions/commit/5a7158a0af74a0d) Leon Loeser *2013-07-28 21:30:23*

**New Block, Fixed Bug**

 * New Block:  ME Item Dropper
 * Fixed Bug:  Use other method for Block Placement, fixed

[55bd81566688435](https://git.the9grounds.com/minecraft/aeadditions/commit/55bd81566688435) Leon Loeser *2013-07-28 14:10:31*

**Fixed SetUp**


[863dc5035296205](https://git.the9grounds.com/minecraft/aeadditions/commit/863dc5035296205) Leon Loeser *2013-07-17 14:00:40*

**Updated to MC  1.6.2**


[62897b6cd481b98](https://git.the9grounds.com/minecraft/aeadditions/commit/62897b6cd481b98) Leon Loeser *2013-07-17 13:16:11*

**Fixed Exoloit (Bug)**

 * Added Parameters to SolderingPacket, so it syncs up-/downgrades with
 * server!

[2bc5d32c168cdc2](https://git.the9grounds.com/minecraft/aeadditions/commit/2bc5d32c168cdc2) Leon Loeser *2013-07-15 15:56:39*

**Stuff**


[7b68e4699da5313](https://git.the9grounds.com/minecraft/aeadditions/commit/7b68e4699da5313) Leon Loeser *2013-07-14 13:41:27*

**Update README.md**


[6d3602a56d28b3b](https://git.the9grounds.com/minecraft/aeadditions/commit/6d3602a56d28b3b) Pwnie2012 *2013-06-21 15:52:31*

**Added preformatted tags to the storages**


[88f1d677fec25dd](https://git.the9grounds.com/minecraft/aeadditions/commit/88f1d677fec25dd) Leon Loeser *2013-06-21 13:42:02*

**Adjustable ME Storage gets nbt-data onCrafting**


[1fc7e2699449f2a](https://git.the9grounds.com/minecraft/aeadditions/commit/1fc7e2699449f2a) Leon Loeser *2013-06-17 18:41:25*

**Made Adjustable ME Cell craftable and added cost to size and types**


[f90be73a79cc7ca](https://git.the9grounds.com/minecraft/aeadditions/commit/f90be73a79cc7ca) Leon Loeser *2013-06-13 14:20:45*

**New Textures (by cyntain)**


[1c53fa325370761](https://git.the9grounds.com/minecraft/aeadditions/commit/1c53fa325370761) Leon Loeser *2013-06-12 18:55:01*

**Updated to AppEng 11-rc4**


[ea62b663bf88ff3](https://git.the9grounds.com/minecraft/aeadditions/commit/ea62b663bf88ff3) Leon Loeser *2013-06-11 18:14:10*

**Updated API**


[59eedfa587691e8](https://git.the9grounds.com/minecraft/aeadditions/commit/59eedfa587691e8) Leon Loeser *2013-06-11 17:14:18*

**Updated to Applied Energistics 11-rc3**


[b5f839861667751](https://git.the9grounds.com/minecraft/aeadditions/commit/b5f839861667751) Leon Loeser *2013-06-11 16:55:45*

**Added mcmod.info**


[c7b039eb05f1a9d](https://git.the9grounds.com/minecraft/aeadditions/commit/c7b039eb05f1a9d) Leon Loeser *2013-06-11 16:33:28*

**Updated README.md**


[87ab8707d33a76d](https://git.the9grounds.com/minecraft/aeadditions/commit/87ab8707d33a76d) Pwnie2012 *2013-06-11 16:16:36*

**Edited BuildScript**


[f0b503c52dc43ee](https://git.the9grounds.com/minecraft/aeadditions/commit/f0b503c52dc43ee) Leon Loeser *2013-06-11 16:13:11*

**added buildscript**


[5de9146660ebe1e](https://git.the9grounds.com/minecraft/aeadditions/commit/5de9146660ebe1e) Leon Loeser *2013-06-11 16:00:44*

**Fixed Shift-Right-Click Bug**


[c4d5e1c10b72299](https://git.the9grounds.com/minecraft/aeadditions/commit/c4d5e1c10b72299) Leon Loeser *2013-06-11 15:47:01*

**Updated the textures**

 * Basically just adjusted the default AE ones

[a4c6cdcb319a7c9](https://git.the9grounds.com/minecraft/aeadditions/commit/a4c6cdcb319a7c9) PaleoCrafter *2013-06-10 20:00:49*

**Create README.md**


[b59a8eee2d37ba6](https://git.the9grounds.com/minecraft/aeadditions/commit/b59a8eee2d37ba6) Pwnie2012 *2013-06-10 19:27:20*

**Changed file structure**

 * Got soldering station working

[d5b2395b7beadf5](https://git.the9grounds.com/minecraft/aeadditions/commit/d5b2395b7beadf5) PaleoCrafter *2013-06-10 19:25:19*


 