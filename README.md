# Android Lock Screen Manager (TrustAgent)

The Trust Agent defined in this projects coordinates challenges with the challenge lock screen. Examples for challenges can be found in [Demo Challenges](https://github.com/notalexa/android_demoapps#readme).

## Installation

We assume you have the [AOSP Project](https://github.com/notalexa/aosp_extensions#readme) already in place. This implies a working eclipse and a successful build of the AOSP. This repository is downloaded and imported as a project in eclipse.

Edit the file `.projects` and set the variable `AOSP_HOME` to the correct place. Create the directory `$AOSP_HOME/packages/apps/UnlockTrustAgent` and copy everything everything from `src/main`into `aosp` (which is a link to the directory just created). The project should compile now. To package the app into the various build, uncomment the line containing `UnlockTrustAgent` in `build/make/target/product/mainline_product.mk`.

All icons are taken or derived from the [OpenMoji Project](https://openmoji.org).

## Description

Trust Agents are configured in the Security section of the general settings. A PIN, passsword or pattern needs to be enabled. If enabled, the agent should be shown in the first section. If not, you have to enable the trust agent directly (in the advanced section). After entering the PIN, the main configuration page of the trust agent shows up. All challenges installed on the system should show up and can be enabled.

In the advanced section, you can enable boot login without a PIN (or pattern). This is handy for smartphones used by children. If a fingerprint is registered, this is sufficient to login even after a reboot. The PIN can be kept secret (and all configurations needing the PIN also).

Furthermore, the agent can grant specific permissions. For example, if a biometric permission is automatically granted, only a challenge is needed to fulfill a weak two factory authentication, handy for usage at home.

## Outlook

Not yet implemented, the manager should enable a generic API for external apps granting specific permissions. For example, a network observer app may grant location privileges based on the availability of certain networks.

 