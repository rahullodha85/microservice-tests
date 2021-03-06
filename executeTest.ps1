param([string]$test)
$msg="Running test for tags: " + $test
$tags=""
if($test -eq "")
{
    $command="mvn clean test"
    Write-Host "No tags selected, running test for all services"
    Invoke-Expression $command
}
else
{
    foreach($tag in $test.split(","))
    {
        if($tags -eq "")
        {
            $tags=$tags + "@" + $tag
        }
        else
        {
            $tags=$tags + ",@" + $tag
        }
    }
    $command="mvn clean test '-Dcucumber.options=--tags $tags'"
    Write-Host "Running Command" $command
    Invoke-Expression $command
}