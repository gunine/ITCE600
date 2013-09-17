"""Custom topology example

Two directly connected switches plus a host for each switch:

    host1___switch1_____switch2___switch3_____switch5___host2
	           \                         /
	            \                       / 
	             \                     /
                      \______switch4______/

Adding the 'topos' dict with a key/value pair to generate our newly defined
topology enables one to pass in '--topo=mytopo' from the command line.
"""

from mininet.topo import Topo

class MyTopo( Topo ):
    "Simple topology example."

    def def_nopts( self, node, name):
        '''Return default dict for a topo.

        @param node type of node
        @param name name of node
        @return d dict with layer key/val pair, plus anything else (later)
        '''
	key = "%i_%i" % (node, name)
        d = {'key': key}

        # For hosts only, set the IP
        mac = "00:00:00:00:%02x:%02x" % (node, name)
        ip = "10.0.%i.%i" % (node, name)

	fb, lb = [int(s) for s in key.split('_')]

	dpid = (fb << 8) + lb
        d.update({'ip': ip})
        d.update({'mac': mac})
        d.update({'dpid': "%016x" % dpid})
	return d

    def __init__( self ):
        "Create custom topo."

        # Initialize topology
        Topo.__init__( self )

        # Add hosts and switches
	host1_opts = self.def_nopts( 1, 1 )
        host1 	= self.addHost(   'h1', **host1_opts )

	host2_opts = self.def_nopts( 1, 2 )
        host2 	= self.addHost(   'h2', **host2_opts )

	switch1_opts = self.def_nopts( 2, 1 )
        switch1 = self.addSwitch( 's1', **switch1_opts )

	switch2_opts = self.def_nopts( 2, 2 )
	switch2 = self.addSwitch( 's2', **switch2_opts )

	switch3_opts = self.def_nopts( 2, 3 )
	switch3 = self.addSwitch( 's3', **switch3_opts )

	switch4_opts = self.def_nopts( 2, 4 )
	switch4 = self.addSwitch( 's4', **switch4_opts )

	switch5_opts = self.def_nopts( 2, 5 )
	switch5 = self.addSwitch( 's5', **switch5_opts )

        # Add links
        self.addLink(   host1, switch1 )
	self.addLink( switch1, switch2 )
	self.addLink( switch2, switch3 )
	self.addLink( switch3, switch5 )
	self.addLink( switch1, switch4 )
	self.addLink( switch4, switch5 )
	self.addLink( switch5, host2   )

topos = { 'mytopo': ( lambda: MyTopo() ) }
