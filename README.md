# Licob
## Description
Licob is an old well-known backup tool. It allows to create ordered chains of rules and then run a script.
## An example
Rule 1 uses the folder type and the home ( /home/user ) directory as a source and excludes /home/user/Movies and /home/user/Download directories.
Rule 2 uses the file type and copies only /home/user/Movies/your_favorite_movie.mkv file.
At the end the program executes the following script to save a list of the installed packages:
```
#!/bin/bash

dpkg --get-selections | grep -w install > /directory/install_packeges
```