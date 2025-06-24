
#准备上传的类库 module
upArray=(
#  ":CloudBase"

#  ":CloudAnnotations"

#  ":CloudPlugin"

#  ":CloudApi"

  ":CloudCompiler"
)

type_params="r"

for element in "${upArray[@]}"
do
  if [ "$1" = $type_params ]; then
    ./gradlew "$element:uploadRemote" < /dev/null
  else
    ./gradlew "$element:uploadLocal" < /dev/null
  fi
done

if [ "$1" = $type_params ]; then
    echo "SDK 发布远程结束"
else
    echo "SDK 发布本地结束"
fi
