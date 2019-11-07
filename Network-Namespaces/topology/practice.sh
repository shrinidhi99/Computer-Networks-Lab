sudo ip netns add n0
sudo ip netns add n1
sudo ip netns add n2

sudo ip link add name br0 type bridge
sudo ip link add name br1 type bridge
sudo ip link add name br2 type bridge

sudo ip link set dev br0 up
sudo ip link set dev br1 up
sudo ip link set dev br2 up

sudo ip link add n0-br0 type veth peer name br0-n0
sudo ip link add n1-br1 type veth peer name br1-n1
sudo ip link add n2-br2 type veth peer name br2-n2

sudo ip link add br0-br1 type veth peer name br1-br0
sudo ip link add br1-br2 type veth peer name br2-br1
sudo ip link add br2-br0 type veth peer name br0-br2

sudo ip link set n0-br0 netns n0
sudo ip link set n1-br1 netns n1
sudo ip link set n2-br2 netns n2

sudo ip link set br0-n0 master br0
sudo ip link set br1-n1 master br1
sudo ip link set br2-n2 master br2

sudo ip link set br0-br1 master br0
sudo ip link set br1-br0 master br1
sudo ip link set br1-br2 master br1
sudo ip link set br2-br1 master br2
sudo ip link set br2-br0 master br2
sudo ip link set br0-br2 master br0

sudo ip netns exec n0 ip link set dev n0-br0 up
sudo ip netns exec n1 ip link set dev n1-br1 up
sudo ip netns exec n2 ip link set dev n2-br2 up

sudo ip link set dev br0-n0 up
sudo ip link set dev br1-n1 up
sudo ip link set dev br2-n2 up

sudo ip link set dev br0-br1 up
sudo ip link set dev br1-br2 up
sudo ip link set dev br2-br0 up

sudo ip link set dev br1-br0 up
sudo ip link set dev br2-br1 up
sudo ip link set dev br0-br2 up

sudo ip netns exec n0 ip address add 10.0.0.1/24 dev n0-br0
sudo ip netns exec n1 ip address add 10.0.0.5/24 dev n1-br1
sudo ip netns exec n2 ip address add 10.0.0.9/24 dev n2-br2

sudo ip address add 10.0.0.2/24 dev br0-n0
sudo ip address add 10.0.0.6/24 dev br1-n1
sudo ip address add 10.0.0.10/24 dev br2-n2

sudo ip address add 10.0.0.3/24 dev br0-br1
sudo ip address add 10.0.0.4/24 dev br1-br0

sudo ip address add 10.0.0.7/24 dev br1-br2
sudo ip address add 10.0.0.8/24 dev br2-br1

sudo ip address add 10.0.0.11/24 dev br2-br0
sudo ip address add 10.0.0.12/24 dev br0-br2

sudo ip netns exec n0 bash

sudo ip netns exec n0 ip route add default via 10.0.0.2 dev n0-br0
sudo ip netns exec n1 ip route add default via 10.0.0.6 dev n1-br1
sudo ip netns exec n2 ip route add default via 10.0.0.10 dev n2-br2

sudo ip netns exec n0 ping 10.0.0.1
sudo ip netns exec n1 ping 10.0.0.5
sudo ip netns exec n2 ping 10.0.0.9

sudo ip netns exec n0 ip link del n0-br0
sudo ip netns exec n1 ip link del n1-br1
sudo ip netns exec n2 ip link del n2-br2

sudo ip link del br0-br1
sudo ip link del br1-br2
sudo ip link del br2-br0

sudo ip link del br0
sudo ip link del br1
sudo ip link del br2

sudo ip netns del n0
sudo ip netns del n1
sudo ip netns del n2
