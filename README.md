# M3 Documentation

##### All code is in __src__ folder
##### __Anonimization__: Anonimize MAC addresses to share data with other projects
##### __Analysis__: __Analysis.java__: Separate globally unique and locally generated MAC addresses
######               __monitor.ipnyb__: Visualize the activity of a specific MAC address in Benton
##### __Extraction__: __Extract.java__: Combine data collected from different raspberry pis
######                 __Time.py__: Remove time different of data from a specific raspberry pi
######                 __Extract.ipnyb__: Visualize trend of all devices in Benton
##### __Localization__: __Combine.py__: Combine result from __Extraction/Extract.java__ with AP data
######                   __Model.py__: Build deep learning model to predict location of a device based on its signal strength received by raspberry pis
######                   __Localize__: Apply model to predict and visualize location of a device based on its signal strength received by raspberry pis
##### __Read__: Extract vendors data feature
##### __TrilateralLocalization__: Trilateral localization method
##### __APExtract.java__: Collect AP data

##### More detail is described in each file
