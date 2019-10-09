## Install LXC
* use **sudo** keyword wherever necessary

>_apt-get install lxc lxc-templates -y_

* After installing LXC, you can **check** the LXC using the following command:

>_lxc-checkconfig_

## Create an LXC Container

* LXC comes with lots of **ready-made templates** for creating Linux containers. You can list out them by running the following command:

> _ls /usr/share/lxc/templates/_

* **create** your first Ubuntu container with the following command:

>_lxc-create -n new-container -t ubuntu_

* You should see that the Ubuntu container created with the default user ubuntu and password ubuntu. You can now **list** the created container with the following command:

>_lxc-ls_

## Start Container

*  You can now **start** the container with the following command:

>_lxc-start -n new-container -d_

Next, take a **console** of the container by running the following command:

>_lxc-console -n new-container_

## Working With an LXC Container

* You can get the **details** of the running container using the following command:

>_lxc-info -n new-container_

* You can **start** and **stop** the container with the following command:

> _lxc-freeze -n new-container_

> _lxc-unfreeze -n new-container_

* You can also take a **snapshot** of the container. First, stop the running container with the following command:

> _lxc-stop -n new-container_

> _lxc-snapshot -n new-container_

* The **snapshot** will be present in a location which you can get by:

> _lxc-snapshot -L -n new-container_

* You can **restore** the snapshot using the following command:

> _lxc-snapshot -r snap0 -n new-container_

* If you want to **delete** the container, run the following command:

>_lxc-stop -n new-container_

>_lxc-destroy -n new-container_

