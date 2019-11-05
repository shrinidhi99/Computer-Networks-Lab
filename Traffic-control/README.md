```shell
sudo ip netns add red
sudo ip netns add blue
sudo ip link add tap0 type veth peer name tap1
sudo ip link set tap0 netns red
sudo ip link set tap1 netns blue
sudo ip netns exec red ip link set tap0 up
sudo ip netns exec blue ip link set tap1 up
sudo ip netns exec red ip address add 10.0.0.1/24 dev tap0
sudo ip netns exec blue ip address add 10.0.0.2/24 dev tap1
ip netns exec red bash
tc qdisc add dev tap0 root tbf rate 2.5mbit burst 5kb latency 20
iperf -c 10.0.0.2

tc qdisc replace dev tap0 root netem rate 5kbit
iperf -c 10.0.0.2

tc qdisc replace dev tap0 root netem delay 10ms
iperf -c 10.0.0.2
```
