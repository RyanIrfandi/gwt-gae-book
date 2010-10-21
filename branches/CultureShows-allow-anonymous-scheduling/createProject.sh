#!/usr/bin/env bash
package=$1
if [ -z "$package" ]
then
        echo "Usage: createProject.sh com.your.package"
	exit
fi

hierarchy=($(echo $package | tr '.' ' '))
project=${hierarchy[2]}
#TODO only 3 levels are supported now, e.g. can't handle com.your.package.goes.here 
tar xfz template.tgz
mv template $project
cd ${hierarchy[2]}
top_folder=$(pwd)

cd src
find . -type f -exec sed -i "s/org.gwtgaebook.template/$package/g" {} \;

#rename folders
mv org ${hierarchy[0]}
cd ${hierarchy[0]}
mv gwtgaebook ${hierarchy[1]}
cd ${hierarchy[1]}
mv template ${hierarchy[2]}

#additional package replaces
cd $top_folder
sed -i "s/org.gwtgaebook.template/$package/g" war/WEB-INF/web.xml
sed -i "s/<name>template<\/name>/<name>$project<\/name>/g" .project
sed -i "s/\/template/\/$project/g" .factorypath

