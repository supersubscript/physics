awk '                                                                                                          
BEGIN {FS=OFS=" "}
{
sum=0; n=0;
for(i=2;i<=NF;i++)
     {if($i > 0){sum+=$i; ++n}}
     if(n>0)print sum/n
}' $1  > $2 

