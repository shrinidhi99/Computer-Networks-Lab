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


---

### Star topology with three namespaces with a linux bridge at the centre

> Create three namespaces
```shell
sudo ip netns add red
sudo ip netns add blue
sudo ip netns add green
```
> Create veth pairs
```shell
sudo ip link add eth0 type veth peer name eth1
sudo ip link add eth2 type veth peer name eth3
sudo ip link add eth4 type veth peer name eth5
```
> Set the veth interfaces inside the namespaces
```shell
sudo ip link set eth0 netns red
sudo ip link set eth2 netns green
sudo ip link set eth4 netns blue
```
> Bring loopback interfaces up
```shell
sudo ip netns exec red ip link set lo up
sudo ip netns exec green ip link set lo up
sudo ip netns exec blue ip link set lo up
```
> Bring up the interfaces within namespaces
```shell
sudo ip netns exec red ip link set eth0 up
sudo ip netns exec green ip link set eth2 up
sudo ip netns exec blue ip link set eth4 up
```
> Assign interfaces within namespaces IP addresses
```shell
sudo ip netns exec red ip address add 10.0.0.1/24 dev eth0
sudo ip netns exec green ip address add 10.0.0.3/24 dev eth2
sudo ip netns exec blue ip address add 10.0.0.5/24 dev eth4
```
> Create bridge using iproute package. Brctl is deprecated
```shell
sudo ip link add name br0 type bridge
sudo ip link set dev br0 up
```
> Set the other lose interfaces into the bridge
```shell
sudo ip link set eth1 master br0
sudo ip link set eth3 master br0
sudo ip link set eth5 master br0
```
> Bring bridge interfaces up
```shell
sudo ip link set dev eth1 up
sudo ip link set dev eth3 up
sudo ip link set dev eth5 up
```
> Now ping, it works
```shell
sudo ip netns exec red ping 10.0.0.3
sudo ip netns exec red ping 10.0.0.5
sudo ip netns exec green ping 10.0.0.5
```
> Add network loop into br0 and set the interfaces up
```shell
sudo ip link add eth11 type veth peer name eth22
sudo ip link set eth11 master br0
sudo ip link set eth22 master br0
sudo ip link set dev eth11 up
sudo ip link set dev eth22 up
```
> Delete old arp entries
```shell
sudo ip netns exec red arp -d 10.0.0.5
sudo ip netns exec red arp -d 10.0.0.3
```
> Ping won’t work. Reason: Network loop.
```shell
sudo ip netns exec green ping 10.0.0.5
sudo ip netns exec red ping 10.0.0.3
```

---

### Star topology with three namespaces with one namespace (router) at the centre

> Create three namespaces
```shell
sudo ip netns add red
sudo ip netns add green
sudo ip netns add blue
```
> Create three veth pairs
```shell
sudo ip link add eth0 type veth peer name eth1
sudo ip link add eth2 type veth peer name eth3
sudo ip link add eth4 type veth peer name eth5
```
> Set them into the namespaces
```shell
sudo ip link set eth0 netns red
sudo ip link set eth2 netns green
sudo ip link set eth4 netns blue
```
> Bring them up
```shell
sudo ip netns exec red ip link set eth0 up
sudo ip netns exec green ip link set eth2 up
sudo ip netns exec blue ip link set eth4 up
```
> Assign IP address to them (all different subnets)
```shell
sudo ip netns exec red ip address add 10.0.0.1/24 dev eth0
sudo ip netns exec green ip address add 10.0.2.1/24 dev eth2
sudo ip netns exec blue ip address add 10.0.4.1/24 dev eth4
```
> Create router namespace and add appropriate interfaces into it
```shell
sudo ip netns add router
sudo ip link set eth1 netns router
sudo ip link set eth3 netns router
sudo ip link set eth5 netns router
```
> Bring the interface up
```shell
sudo ip netns exec router ip link set eth1 up
sudo ip netns exec router ip link set eth3 up
sudo ip netns exec router ip link set eth5 up
```
> Assign IP addresses to the interfaces within router
```shell
sudo ip netns exec router ip address add 10.0.0.2/24 dev eth1
sudo ip netns exec router ip address add 10.0.2.2/24 dev eth3
sudo ip netns exec router ip address add 10.0.4.2/24 dev eth5
```
> Bring loopback interfaces up
```shell
sudo ip netns exec router ip link set lo up
sudo ip netns exec red ip link set lo up
sudo ip netns exec blue ip link set lo up
sudo ip netns exec green ip link set lo up
```
> Try ping, won’t work as there is no way to know where to send packets
> Add default gateway, i.e. it serves as a forwarding host to connect to other networks
```shell
sudo ip netns exec red ip route add default via 10.0.0.2 dev eth0
sudo ip netns exec green ip route add default via 10.0.2.2 dev eth2
sudo ip netns exec blue ip route add default via 10.0.4.2 dev eth4
```
> Enable IP forwarding : Make a system to act as a router i.e., it should determine the path a
packet has to take to reach it’s destination
```shell
sudo ip netns exec router sysctl -w net.ipv4.ip_forward=1
```
> Try ping now, it works
```shell
sudo ip netns exec blue ping 10.0.4.1
sudo ip netns exec green ping 10.0.2.1
sudo ip netns exec red ping 10.0.0.1
```
---
### Star topology with 4 namespaces with one namespace (router) at the centre

> Create three namespaces
```shell
sudo ip netns add red
sudo ip netns add green
sudo ip netns add blue
sudo ip netns add grey
```
> Create three veth pairs
```shell
sudo ip link add eth0 type veth peer name eth1
sudo ip link add eth2 type veth peer name eth3
sudo ip link add eth4 type veth peer name eth5
sudo ip link add eth6 type veth peer name eth7
```
> Set them into the namespaces
```shell
sudo ip link set eth0 netns red
sudo ip link set eth2 netns green
sudo ip link set eth4 netns blue
sudo ip link set eth6 netns grey
```
> Bring them up
```shell
sudo ip netns exec red ip link set eth0 up
sudo ip netns exec green ip link set eth2 up
sudo ip netns exec blue ip link set eth4 up
sudo ip netns exec grey ip link set eth6 up
```
> Assign IP address to them (all different subnets)
```shell
sudo ip netns exec red ip address add 10.0.0.1/24 dev eth0
sudo ip netns exec green ip address add 10.0.2.1/24 dev eth2
sudo ip netns exec blue ip address add 10.0.4.1/24 dev eth4
sudo ip netns exec grey ip address add 10.0.6.1/24 dev eth6
```
> Create router namespace and add appropriate interfaces into it
```shell
sudo ip netns add router
sudo ip link set eth1 netns router
sudo ip link set eth3 netns router
sudo ip link set eth5 netns router
sudo ip link set eth7 netns router
```
> Bring the interface up
```shell
sudo ip netns exec router ip link set eth1 up
sudo ip netns exec router ip link set eth3 up
sudo ip netns exec router ip link set eth5 up
sudo ip netns exec router ip link set eth7 up
```
> Assign IP addresses to the interfaces within router
```shell
sudo ip netns exec router ip address add 10.0.0.2/24 dev eth1
sudo ip netns exec router ip address add 10.0.2.2/24 dev eth3
sudo ip netns exec router ip address add 10.0.4.2/24 dev eth5
sudo ip netns exec router ip address add 10.0.6.2/24 dev eth7
```
> Bring loopback interfaces up
```shell
sudo ip netns exec router ip link set lo up
sudo ip netns exec red ip link set lo up
sudo ip netns exec blue ip link set lo up
sudo ip netns exec green ip link set lo up
sudo ip netns exec grey ip link set lo up
```
> Try ping, won’t work as there is no way to know where to send packets
> Add default gateway, i.e. it serves as a forwarding host to connect to other networks
```shell
sudo ip netns exec red ip route add default via 10.0.0.2 dev eth0
sudo ip netns exec green ip route add default via 10.0.2.2 dev eth2
sudo ip netns exec blue ip route add default via 10.0.4.2 dev eth4
sudo ip netns exec grey ip route add default via 10.0.6.2 dev eth6
```
> Enable IP forwarding : Make a system to act as a router i.e., it should determine the path a packet has to take to reach it’s destination
```shell
sudo ip netns exec router sysctl -w net.ipv4.ip_forward=1
```
> Try ping now, it works
```shell
sudo ip netns exec blue ping 10.0.4.1
sudo ip netns exec green ping 10.0.2.1
sudo ip netns exec red ping 10.0.0.1
sudo ip netns exec grey ping 10.0.6.1
```
---

### Create a network by connecting 3 linux bridges (each connected to a different network namespace) in a linear fashion (1--2--3)

    netns1 -- br1 -- br2 -- br3 -- netns3
                      |
                    netns2

> Create three namespaces
```shell
sudo ip netns add netns1
sudo ip netns add netns2
sudo ip netns add netns3
```
> Create veth pairs
```shell
sudo ip link add eth0 type veth peer name eth1
sudo ip link add eth2 type veth peer name eth3
sudo ip link add eth4 type veth peer name eth5

sudo ip link add eth6 type veth peer name eth7
sudo ip link add eth8 type veth peer name eth9
```
> Set the veth interfaces inside the namespaces
```shell
sudo ip link set eth0 netns netns1
sudo ip link set eth2 netns netns2
sudo ip link set eth4 netns netns3
```
> Bring loopback interfaces up
```shell
sudo ip netns exec netns1 ip link set lo up
sudo ip netns exec netns2 ip link set lo up
sudo ip netns exec netns3 ip link set lo up
```
> Bring up the interfaces within namespaces
```shell
sudo ip netns exec netns1 ip link set eth0 up
sudo ip netns exec netns2 ip link set eth2 up
sudo ip netns exec netns3 ip link set eth4 up
```
> Assign interfaces within namespaces IP addresses
```shell
sudo ip netns exec netns1 ip address add 10.0.0.1/24 dev eth0
sudo ip netns exec netns2 ip address add 10.0.0.3/24 dev eth2
sudo ip netns exec netns3 ip address add 10.0.0.5/24 dev eth4
```
> Create 3 bridges using iproute package. 
```shell
sudo ip link add name br1 type bridge
sudo ip link set dev br1 up

sudo ip link add name br2 type bridge
sudo ip link set dev br2 up

sudo ip link add name br3 type bridge
sudo ip link set dev br3 up
```
> Set the other lose interfaces into the bridge. eth1 -> br1, eth2 -> br2, eth3 -> br3
```shell
sudo ip link set eth1 master br1
sudo ip link set eth3 master br2
sudo ip link set eth5 master br3

sudo ip link set eth6 master br1
sudo ip link set eth7 master br2

sudo ip link set eth8 master br2
sudo ip link set eth9 master br3
```
> Bring bridge interfaces up
```shell
sudo ip link set dev eth1 up
sudo ip link set dev eth3 up
sudo ip link set dev eth5 up

sudo ip link set dev eth6 up
sudo ip link set dev eth7 up
sudo ip link set dev eth8 up
sudo ip link set dev eth9 up
```
> Now ping, it works
```shell
sudo ip netns exec netns1 ping 10.0.0.3
sudo ip netns exec netns2 ping 10.0.0.5
sudo ip netns exec netns3 ping 10.0.0.5
```
> Add network loop into br0 and set the interfaces up
```shell
sudo ip link add eth11 type veth peer name eth12
sudo ip link set eth11 master br1
sudo ip link set eth12 master br1
sudo ip link set dev eth11 up
sudo ip link set dev eth12 up

sudo ip link add eth21 type veth peer name eth22
sudo ip link set eth21 master br2
sudo ip link set eth22 master br2
sudo ip link set dev eth21 up
sudo ip link set dev eth22 up

sudo ip link add eth31 type veth peer name eth32
sudo ip link set eth31 master br3
sudo ip link set eth32 master br3
sudo ip link set dev eth31 up
sudo ip link set dev eth32 up
```

---

### Create a network by connecting 3 linux bridges (each connected to a different network namespace) in a circular fashion (1--2--3--1)
                  ns2
                   |
    ns1 -- br1 -- br2 -- br3 -- ns3
            |             |
            +-------------+

> Create three namespaces
```shell
sudo ip netns add netns1
sudo ip netns add netns2
sudo ip netns add netns3
```
> Create veth pairs
```shell
sudo ip link add eth0 type veth peer name eth1
sudo ip link add eth2 type veth peer name eth3
sudo ip link add eth4 type veth peer name eth5

sudo ip link add eth6 type veth peer name eth7
sudo ip link add eth8 type veth peer name eth9
sudo ip link add eth10 type veth peer name eth11
```
> Set the veth interfaces inside the namespaces
```shell
sudo ip link set eth0 netns netns1
sudo ip link set eth2 netns netns2
sudo ip link set eth4 netns netns3
```
> Bring loopback interfaces up
```shell
sudo ip netns exec netns1 ip link set lo up
sudo ip netns exec netns2 ip link set lo up
sudo ip netns exec netns3 ip link set lo up
```
> Bring up the interfaces within namespaces
```shell
sudo ip netns exec netns1 ip link set eth0 up
sudo ip netns exec netns2 ip link set eth2 up
sudo ip netns exec netns3 ip link set eth4 up
```
> Assign interfaces within namespaces IP addresses
```shell
sudo ip netns exec netns1 ip address add 10.0.0.1/24 dev eth0
sudo ip netns exec netns2 ip address add 10.0.0.3/24 dev eth2
sudo ip netns exec netns3 ip address add 10.0.0.5/24 dev eth4
```
> Create 3 bridges using iproute package. 
```shell
sudo ip link add name br1 type bridge
sudo ip link set dev br1 up

sudo ip link add name br2 type bridge
sudo ip link set dev br2 up

sudo ip link add name br3 type bridge
sudo ip link set dev br3 up
```
> Set the other lose interfaces into the bridge. eth1 -> br1, eth2 -> br2, eth3 -> br3
```shell
sudo ip link set eth1 master br1
sudo ip link set eth3 master br2
sudo ip link set eth5 master br3

sudo ip link set eth6 master br1
sudo ip link set eth7 master br2

sudo ip link set eth8 master br2
sudo ip link set eth9 master br3

sudo ip link set eth10 master br3
sudo ip link set eth11 master br1
```
> Bring bridge interfaces up
```shell
sudo ip link set dev eth1 up
sudo ip link set dev eth3 up
sudo ip link set dev eth5 up

sudo ip link set dev eth6 up
sudo ip link set dev eth7 up
sudo ip link set dev eth8 up
sudo ip link set dev eth9 up
sudo ip link set dev eth10 up
sudo ip link set dev eth11 up
```
> Now ping, it works
```shell
sudo ip netns exec netns1 ping 10.0.0.3
sudo ip netns exec netns2 ping 10.0.0.5
sudo ip netns exec netns3 ping 10.0.0.5
```
> Add network loop into br0 and set the interfaces up
```shell
sudo ip link add eth11 type veth peer name eth12
sudo ip link set eth11 master br1
sudo ip link set eth12 master br1
sudo ip link set dev eth11 up
sudo ip link set dev eth12 up

sudo ip link add eth21 type veth peer name eth22
sudo ip link set eth21 master br2
sudo ip link set eth22 master br2
sudo ip link set dev eth21 up
sudo ip link set dev eth22 up

sudo ip link add eth31 type veth peer name eth32
sudo ip link set eth31 master br3
sudo ip link set eth32 master br3
sudo ip link set dev eth31 up
sudo ip link set dev eth32 up
```

---