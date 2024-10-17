
#准备上传的类库 module
upArray=(
#  ":CloudBase"

#  ":CloudAnnotations"

  ":CloudPlugin"

#  ":CloudApi"

#  ":CloudCompiler"

)

type_params="release"

for element in "${upArray[@]}"
do
  if [ "$1" = $type_params ]; then
    ./gradlew "$element:uploadRemote" < /dev/null
  else
    ./gradlew "$element:uploadLocal" < /dev/null
  fi
done

if [ "$1" = $type_params ]; then
    echo "Release 发布版 SDK 上传结束"
else
    echo "SnapShot 快照版 SDK 上传结束"
fi
