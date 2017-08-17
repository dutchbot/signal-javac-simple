# install JCE into

https://stackoverflow.com/questions/6481627/java-security-illegal-key-size-or-default-parameters

replace files `local_policy.jar` and `US_export_policy.jar` under e.g:
`C:\Program Files\Java\jre1.8.0_141\lib\security`

Also install BouncyCastle provider in java lib.

## Note

When using GCM, if the other party sends back a message, the gcm id unregisters.
You can still send messages, but no longer receive them.
This seems to be some kind of bug with GCM or signal server.