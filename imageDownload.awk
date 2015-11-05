BEGIN {
	FS=";"	
}
	#match($0, /www.wga.hu\/.*[.]html/) { print substr($0, RSTART, RLENGTH) }
{
	for (i = 1; i <= NF; i++) {
		if ($i ~ /www/) {
			sub(/[.]html/, "", $i)
			html_start = match($i, /html/)
			print $i "(Good part starts at: " html_start
			print "\t>>> " substr($i, 24, 1000)
			image_url = "http://www.wga.hu/art/" substr($i, html_start + 5, 1000) ".jpg"
			print "\t>>> Image URL:\t" image_url
			system("wget " "-Pimages/ " image_url)
		}
	}
}
