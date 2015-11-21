for i in $(ls x*)
do
	wget -i $i -N -P../images/ 2> log_files/$i"_"log &
done
