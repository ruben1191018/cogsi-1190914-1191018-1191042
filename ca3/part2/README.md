# CA3

## Part 2

### One VM should host the Spring application (app), while the other should host the H2 database (db)

For this step, first of all, we just split the vagrant file for two vms in vagrant file.

//To continue


### Ensure that your VMs are allocated sufficient resources

To configure the CPU and memory we can use the following configurations in the vagrant file

The name of the machine is "db", in this case.

We defined it to have 1024MB (1GB) of memory and 1 CPU.

![img.png](images/img.png)

To change the disk size we had to install a new plugin called "vagrant-disksize"

After the plugin was installed we used the following config:

config.disksize.size = '5 GB'
After adding the configurations we destroyed and created the VM again with the following commands

vagrant destroy
vagrant up
