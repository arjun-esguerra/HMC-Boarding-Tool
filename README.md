# HMC Boarding Tool

Java and PowerShell GUI application for efficient user onboarding and offboarding at HMC Architects. Automates AD user creation, group policy assignment, shared drive access, Microsoft 365 license management, and Teams phone number configuration. Utilizes Microsoft Exchange Online, Microsoft Teams, and Microsoft Graph SDKs.

## Getting Started

### Dependencies
* Java JDK 22 (included in package)
* Access to Microsoft Graph SDK (configured in Azure Admin Center)
* Access to Microsoft Exchange Online, Microsoft Teams (Local Admin Rights)
* Tested on Windows 11 (Version 10.0.22631 Build 22631)

### Installing

* Download .zip folder and extract contents to the Program Files folder in the C drive. Final file path should be: <br/> C:\Program Files\hmcboarding</br>
* Create a shortcut of the hmcboarding.exe and place it anywhere you would like to run it. Alternatively, run it directly from the folder.

## Instructions

### Running the application
1. Run hmcboarding.exe as administrator.</br>
2. Sign in using your O365 Admin account. This will provide access to Microsoft's Exchange Online SDK, Teams SDK, and Graph SDK.
<br/><br/><img src="https://github.com/arjun-esguerra/HMC-Boarding-Tool/assets/169405197/9a452bc5-054f-47f1-8ade-3718c185cf76" height="250" width="350">
4. After authentication, you will be greeted with the HMC Boarding Tool homescreen. You can select to either onboard or offboard a user.
<br/><br/><img src="https://github.com/arjun-esguerra/HMC-Boarding-Tool/assets/169405197/c9389067-adbb-48c5-841c-84f77939e97f" height="328" width="278">
### Onboarding a user
1. Fill out all fields, then click Onboard User to start the Active Directory user creation, and license/phone number assignment.
<br/><br/><img src="https://github.com/arjun-esguerra/HMC-Boarding-Tool/assets/169405197/ba6e9059-c1a4-4eb2-bd23-6421abd5ad22" height="328" width="278">
2. An elevated powershell terminal will open, showing the newly created user's details, which are to be added to HR's new hire checklist.
<br/><A continuous script will run, until it has been detected that O365 has synced (takes 5-15 minutes) and upon completion, will assign the user Microsoft license and a phone number.
<br/>![image](https://github.com/arjun-esguerra/HMC-Boarding-Tool/assets/169405197/d6ec3df0-d77a-4857-9b65-798f86bf5992)<br/>
### Offboarding a user
1. To offboard a user, select a user from the list, or search for the user using the search bar. (Names blurred for privacy)
<br/><br/><img src="https://github.com/arjun-esguerra/HMC-Boarding-Tool/assets/169405197/af99b180-7721-45e9-9f6d-75c213a7a9eb" height="328" width="278">
2. Select the user's name, then click on "Offboard User" to initialize the group policy, license, and phone number removal.
<br/><br/><img src="https://github.com/arjun-esguerra/HMC-Boarding-Tool/assets/169405197/2818c724-8f0a-42bd-ab3d-70d53674759d" height="328" width="278">

## Author

* Arjun Esguerra 
* arjun.esguerra@hmcarchitects.com

## Version History

* 1.0 Initial Release

