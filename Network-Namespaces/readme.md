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

___

# Link provided:

* **Creating** a network namespace is actually quite easy. Just use this command:

> _ip netns add namespace-name_

* To **verify** that the network namespace has been created, use this command:

> _sudo ip netns list_

## Assigning Interfaces to Network Namespaces

* First, you’d **create** the veth pair:

> _sudo ip link add veth0 type veth peer name veth1_

* You can **verify** that the veth pair was created using this command:

> _sudo ip link list_

* Let’s say that you want to connect the global namespace to the blue namespace. To do that, you’ll need to move one of the veth interfaces to the **blue** namespace using this command:
(**blue** is the network namespace that we had created.)

> _sudo ip link set veth1 netns blue_

* If you then run the ip link list command again, you’ll see that the veth1 interface has disappeared from the list. It’s now in the blue namespace, so to see it you’d need to run this command:

> _sudo ip netns exec blue ip link list_

* Breakdown of the above command:
    * The first part, ip netns exec, is how you execute commands in a different network namespace.
    * Next is the specific namespace in which the command should be run (in this case, the blue namespace).
    * Finally, you have the actual command to be executed in the remote namespace. In this case, you want to see the interfaces in the blue namespace, so you run ip link list.

## Configuring Interfaces in Network Namespaces

* Now that veth1 has been moved to the blue namespace, we need to actually configure that interface. Once again, we’ll use the ip netns exec command, this time to configure the veth1 interface in the **blue** namespace:

> _sudo ip netns exec blue ifconfig veth1 10.1.1.1/24 up_

* You could verify how network namespaces keep the network configuration separate using these commands:

    * _**sudo ip addr list**_ in the global namespace will not show any 10.1.1.0/24-related interfaces or addresses.

    * _**sudo ip netns exec blue ip addr list**_ will show only the 10.1.1.0/24-related interfaces and addresses, and will not show any interfaces or addresses from the global namespace.

    * Similarly, _**sudo ip route list**_ in each namespace will show different routing table entries, including different default gateways.

## Connecting network namespaces to the physical network

> _sudo ip link set dev veth0 netns blue_
* Here **veth0** is the device and **blue** is the network namespace.