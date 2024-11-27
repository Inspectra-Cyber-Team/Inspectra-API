#!/bin/bash

echo "Enter your commit message: "
read commitMessage

git add .

git commit -m "$commitMessage"

git push origin main

echo "Changes have been pushed to the repository!"
