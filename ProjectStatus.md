# OpenFire Project Status #

This is the rough planned feature set for OpenFire v2.0. I've stopped putting target completion dates against tasks, because my life can be so unpredictable as to make mid-term planning pointless.

## [R2](https://code.google.com/p/openfire/source/detail?r=2) Release Plan ##

### OpenFire core library ###

| **Task** | **Status** | **Target/Actual completion date** |
|:---------|:-----------|:----------------------------------|
| Record games currently being played for each friend | COMPLETE   | 2008/06/30                        |
| Conversation specific event listener interface | COMPLETE   | 2008/06/30                        |
| Friend specific event listener interface | COMPLETE   | 2008/06/30                        |

### OpenFire relay ###

| **Task** | **Status** | **Target/Actual completion date** |
|:---------|:-----------|:----------------------------------|
| Group based permissions for commands | Pending    |                                   |
| Send welcome message to newly invited users | Pending    |                                   |
| Command to send message directly to a specific user based on username, ID, IP address or seat number | Pending    |                                   |
| Command to determine the username of the person logged in at a specific IP address | Pending    |                                   |
| Command to determine the username of the person logged in at a specific seat number | Pending    |                                   |
| Command to list all usable aliases for a user (seat number, IP address, ID etc. if applicable) | Pending    |                                   |
| Command to send a message to all users playing on a specific game server (ip+port combination) | Pending    |                                   |
| Authenticate admins and time out admin access | Pending    |                                   |
| Command to silence a user (i.e. prevent messages being relayed) for a set period of time | Pending    |                                   |
| Command to ban a user from the relay permanently | Pending    |                                   |
| Command to list banned users | Pending    |                                   |
| Command to lift a ban on a user | Pending    |                                   |
| Commands to create, subscribe, broadcast to and unsubscribe from channels | Pending    |                                   |
| Store gameplay stats in database | Pending    |                                   |

### OpenFire GUI Client ###

| **Task** | **Status** | **Target/Actual completion date** |
|:---------|:-----------|:----------------------------------|
| Ability to invite users | Pending    |                                   |
| Ability to accept/reject invitations | Pending    |                                   |

## [R1](https://code.google.com/p/openfire/source/detail?r=1) Release Plan ##

[R1](https://code.google.com/p/openfire/source/detail?r=1) was officially released on the 18th of August, 2007.

| **Task** | **Status** | **Target/Actual completion date** |
|:---------|:-----------|:----------------------------------|
| Ability to parse and generate XFire protocol messages | Complete   |  March 23rd 2007                  |
| Classes to manage a TCP/IP connection for sending/receiving messages to the XFire server | Complete   | April 27th 2007                   |
| Representation and maintenance of the buddy list | Complete   | May 26th 2007                     |
| Successfully establish and control a connection with the XFire server | Complete   | May 26th 2007                     |
| Ability to communicate over UDP with other XFire clients| Not relevant for now, TCP suffices | N/A                               |
| Representation and maintenance of conversations with other XFire users | Completed  | 15th June 2007                    |
| Introduction of friend "classes", offline-configured | Completed  | 22nd June 2007                    |
| Basic "admin to user" message relay | Completed  | 29th June 2007                    |
| "user to on-duty admin" relay | Not Completed | 6th July 2007                     |
| Auto-accept invites | Completed  | 12th July 2007                    |