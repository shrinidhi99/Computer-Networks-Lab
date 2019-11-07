sudo ip netns add n0
sudo ip netns add n1

sudo ip link add n0-n1 type veth peer name n1-n0

sudo ip link set n0-n1 netns n0
sudo ip link set n1-n0 netns n1

sudo ip netns exec n0 ip link set n0-n1 up
sudo ip netns exec n1 ip link set n1-n0 up

sudo ip netns exec n0 ip address add 10.0.0.1/24 dev n0-n1
sudo ip netns exec n1 ip address add 10.0.0.2/24 dev n1-n0

sudo ip netns exec n0 tc qdisc add dev n0-n1 root tbf rate 1mbit burst 32kbit latency 400ms

# sudo ip netns exec n0 tc qdisc add dev n0-n1 root tbf rate burst latency  

sudo ip netns exec n0 tc qdisc add dev n0-n1 root netem rate 1mbit

# sudo ip netns exec n0 tc qdisc add dev n0-n1 root netem rate 
sudo ip netns exec n0 tc qdisc add dev n0-n1 netem delay 200ms

# sudo ip netns exec n0 tc qdisc add dev n0-n1 netem delay
# sudo ip netns exec n0 tc qdisc add dev n0-n1 root netem rate delay 

iperf -s
iperf -c ip 

sudo ip netns exec n0 ip link del n0-n1
sudo ip netns del n0
sudo ip netns del n1