document.addEventListener("DOMContentLoaded", function() {
var cy = cytoscape({
			  container: document.getElementById('cy'),
			  elements: [
				  // NODES
				  // Athletics
				  { data: { id: '0', name: 'Lyon Center', continent: 'Athletics', value: '0' }, position: { x: -300, y: -300 }, classes: 'Athletics', },
				  { data: { id: '1', name: 'Dedeaux Field', continent: 'Athletics', value: '0' }, position: { x: -350, y: -250 }, classes: 'Athletics' },
				  { data: { id: '2', name: 'McKay Center', continent: 'Athletics', value: '0' }, position: { x: -250, y: -225 }, classes: 'Athletics' },
				  { data: { id: '3', name: 'Little Galen', continent: 'Athletics', value: '0' }, position: { x: -150, y: -200 }, classes: 'Athletics' },
				  { data: { id: '4', name: 'Marks Tennis Stadium', continent: 'Athletics', value: '0' }, position: { x: -400, y: -200 }, classes: 'Athletics' },
				  { data: { id: '5', name: 'Cromwell Field', continent: 'Athletics', value: '0' }, position: { x: -200, y: -150 }, classes: 'Athletics' },
				  
				  // Dornsife STEM
				  { data: { id: '6', name: 'Kaprelian Hall', continent: 'Dornsife STEM', value: '0' }, position: { x: -450, y: -150 }, classes: 'DSTEM' },
				  { data: { id: '7', name: 'Michelson Center', continent: 'Dornsife STEM', value: '0' }, position: { x: -350, y: -125 }, classes: 'DSTEM' },
				  { data: { id: '8', name: 'Seeley G. Mudd', continent: 'Dornsife STEM', value: '0' }, position: { x: -250, y: -100 }, classes: 'DSTEM' },
				  { data: { id: '9', name: 'Hedco Neuro', continent: 'Dornsife STEM', value: '0' }, position: { x: -150, y: -50 }, classes: 'DSTEM' },
				  { data: { id: '10', name: 'Stauffer Hall', continent: 'Dornsife STEM', value: '0' }, position: { x: -75, y: 100 }, classes: 'DSTEM' },
				  { data: { id: '11', name: 'Zumberge Hall', continent: 'Dornsife STEM', value: '0' }, position: { x: 0, y: 150 }, classes: 'DSTEM' },
				  
				  // Viterbi
				  { data: { id: '12', name: 'Olin Hall', continent: 'Viterbi School of Engineering', value: '0' }, position: { x: -300, y: 10 }, classes: 'Viterbi' },
				  { data: { id: '13', name: 'Tutor Hall', continent: 'Viterbi School of Engineering', value: '0' }, position: { x: -450, y: 50 }, classes: 'Viterbi' },
				  { data: { id: '14', name: 'EQuad', continent: 'Viterbi School of Engineering', value: '0' }, position: { x: -250, y: 80 }, classes: 'Viterbi' },
				  { data: { id: '15', name: 'Vivian Hall', continent: 'Viterbi School of Engineering', value: '0' }, position: { x: -200, y: 30 }, classes: 'Viterbi' },
				  { data: { id: '16', name: 'Salvatori Hall', continent: 'Viterbi School of Engineering', value: '0' }, position: { x: -300, y: 125 }, classes: 'Viterbi' },
				  { data: { id: '17', name: 'Seaver Library', continent: 'Viterbi School of Engineering', value: '0' }, position: { x: -225, y: 150 }, classes: 'Viterbi' },
				  
				  // SCA
				  { data: { id: '23', name: 'Norris Cinema', continent: 'School of Cinematic Arts', value: '0' }, position: { x: -50, y: -400}, classes: 'Cinema' },
				  { data: { id: '24', name: 'SCB', continent: 'School of Cinematic Arts', value: '0' }, position: { x: -150, y: -300}, classes: 'Cinema' },
				  { data: { id: '25', name: 'SCA', continent: 'School of Cinematic Arts', value: '0' }, position: { x: -50, y: -300}, classes: 'Cinema' },
				  
				  // Marshall
				  { data: { id: '18', name: 'Levanthal Hall', continent: 'Marshall School of Business', value: '0' }, position: { x: 100, y: 150 }, classes: 'Marshall' },
				  { data: { id: '19', name: 'Hoffman Hall', continent: 'Marshall School of Business', value: '0' }, position: { x: 175, y: 250 }, classes: 'Marshall' },
				  { data: { id: '20', name: 'Dauterive Hall', continent: 'Marshall School of Business', value: '0' }, position: { x: 300, y: 150 }, classes: 'Marshall' },
				  { data: { id: '21', name: 'Popovich Hall', continent: 'Marshall School of Business', value: '0' }, position: { x: 350, y: 200 }, classes: 'Marshall' },
				  { data: { id: '22', name: 'Fertitta Hall', continent: 'Marshall School of Business', value: '0' }, position: { x: 450, y: 200 }, classes: 'Marshall' },
				  
				  // Dornsife LAC
				  { data: { id: '29', name: 'Tutor Campus Center', continent: 'Dornsife School of LAS', value: '0' }, position: { x: 100, y: 50 }, classes: 'LDornsife' },
				  { data: { id: '26', name: 'Taper Hall', continent: 'Dornsife School of LAS', value: '0' }, position: { x: 60, y: -325 }, classes: 'LDornsife' },
				  { data: { id: '27', name: 'Von KleinSmid Hall', continent: 'Dornsife School of LAS', value: '0' }, position: { x: 150, y: -250 }, classes: 'LDornsife' },
				  { data: { id: '28', name: 'Leavey Library', continent: 'Dornsife School of LAS', value: '0' }, position: { x: 400, y: -300 }, classes: 'LDornsife' },
				  { data: { id: '33', name: 'Doheny Library', continent: 'Dornsife School of LAS', value: '0' }, position: { x: 250, y: -125 }, classes: 'LDornsife' },
				  { data: { id: '31', name: 'Alumni Park', continent: 'Dornsife School of LAS', value: '0' }, position: { x: 150, y: -150 }, classes: 'LDornsife' },
				  { data: { id: '32', name: 'McCarthy Quad', continent: 'Dornsife School of LAS', value: '0' }, position: { x: 400, y: -200 }, classes: 'LDornsife' },
				  { data: { id: '30', name: 'Tommy Trojan', continent: 'Dornsife School of LAS', value: '0' }, position: { x: 200, y: -10 }, classes: 'LDornsife' },
			
				  // Annenberg
				  { data: { id: '34', name: 'Wallis Annenberg Hall', continent: 'Annenberg School of Comm', value: '0' }, position: { x: -50, y: -50 }, classes: 'Annenberg' },
				  { data: { id: '35', name: 'Annenberg School', continent: 'Annenberg School of Comm', value: '0' }, position: { x: 0, y: -150 }, classes: 'Annenberg' },
				  
				  // EDGES
				  { data: { id: 'a', source: '0', target: '1' } },
				  { data: { id: 'b', source: '0', target: '2' } },
				  { data: { id: 'c', source: '0', target: '24' } },
				  { data: { id: 'd', source: '0', target: '2' } },
				  { data: { id: 'e', source: '1', target: '2' } },
				  { data: { id: 'f', source: '1', target: '4' } },
				  { data: { id: 'g', source: '2', target: '3' } },
				  { data: { id: 'h', source: '2', target: '4' } },
				  { data: { id: 'i', source: '2', target: '5' } },
				  { data: { id: 'j', source: '2', target: '24' } },
				  { data: { id: 'k', source: '3', target: '5' } },
				  { data: { id: 'l', source: '3', target: '25' } },
				  { data: { id: 'm', source: '3', target: '34' } },
				  { data: { id: 'n', source: '3', target: '35' } },
				  { data: { id: 'o', source: '4', target: '5' } },
				  { data: { id: 'p', source: '4', target: '6' } },
				  { data: { id: 'q', source: '4', target: '7' } },
				  { data: { id: 'r', source: '5', target: '8' } },
				  { data: { id: 's', source: '5', target: '9' } },
				  { data: { id: 't', source: '5', target: '34' } },
				  
				  { data: { id: 'u', source: '6', target: '7' } },
				  { data: { id: 'v', source: '7', target: '8' } },
				  { data: { id: 'w', source: '7', target: '12' } },
				  { data: { id: 'x', source: '8', target: '9' } },
				  { data: { id: 'y', source: '8', target: '12' } },
				  { data: { id: 'z', source: '9', target: '10' } },
				  { data: { id: 'za', source: '9', target: '15' } },
				  { data: { id: 'zb', source: '9', target: '34' } },
				  { data: { id: 'zc', source: '10', target: '11' } },
				  { data: { id: 'aa', source: '10', target: '15' } },
				  { data: { id: 'bb', source: '10', target: '29' } },
				  { data: { id: 'cc', source: '11', target: '18' } },
				
				  { data: { id: 'dd', source: '12', target: '13' } },
				  { data: { id: 'ee', source: '12', target: '14' } },
				  { data: { id: 'ff', source: '12', target: '15' } },
				  { data: { id: 'gg', source: '13', target: '14' } },
				  { data: { id: 'hh', source: '13', target: '16' } },
				  { data: { id: 'ii', source: '14', target: '15' } },
				  { data: { id: 'jj', source: '14', target: '16' } },
				  { data: { id: 'kk', source: '14', target: '17' } },
				  { data: { id: 'll', source: '15', target: '17' } },
				  { data: { id: 'mm', source: '16', target: '17' } },
				  
				  { data: { id: 'nn', source: '18', target: '19' } },
				  { data: { id: 'oo', source: '18', target: '20' } },
				  { data: { id: 'pp', source: '18', target: '29' } },
				  { data: { id: 'qq', source: '18', target: '30' } },
				  { data: { id: 'rr', source: '19', target: '20' } },
				  { data: { id: 'ss', source: '20', target: '21' } },
				  { data: { id: 'ooo', source: '20', target: '30' } },
				  { data: { id: 'ppp', source: '20', target: '33' } },
				  { data: { id: 'tt', source: '21', target: '22' } },
				  
				  { data: { id: 'uu', source: '23', target: '24' } },
				  { data: { id: 'vv', source: '23', target: '25' } },
				  { data: { id: 'ww', source: '24', target: '25' } },
				  { data: { id: 'xx', source: '25', target: '35' } },
				  
				  { data: { id: 'yy', source: '26', target: '27' } },
				  { data: { id: 'zz', source: '26', target: '28' } },
				  { data: { id: 'aaa', source: '26', target: '35' } },
				  { data: { id: 'bbb', source: '27', target: '28' } },
				  { data: { id: 'ccc', source: '27', target: '31' } },
				  { data: { id: 'ddd', source: '27', target: '32' } },
				  { data: { id: 'nnn', source: '27', target: '35' } },
				  { data: { id: 'eee', source: '28', target: '32' } },
				  { data: { id: 'fff', source: '29', target: '30' } },
				  { data: { id: 'ggg', source: '29', target: '34' } },
				  { data: { id: 'hhh', source: '30', target: '31' } },
				  { data: { id: 'iii', source: '30', target: '34' } },
				  { data: { id: 'jjj', source: '31', target: '32' } },
				  { data: { id: 'kkk', source: '31', target: '33' } },
				  { data: { id: 'lll', source: '32', target: '33' } },
				  
				  { data: { id: 'mmm', source: '34', target: '35' } }
				  
				  
				],
				
				layout:{
					name: 'preset' // To allow manual positioning
				},

			    style: [
			    	{
			            selector: 'node',
			            style: {
			            	shape: 'circle',
			            	label: 'data(value)',
							'text-valign': 'center',
							'text-halign': 'center',
							width: 50,
							height: 50
			            }
			        },
			        {
			            selector: '.Athletics',
			            style: {
			                
			                'border-color': 'darkred',
			                'border-width': 4,
			                'background-color': 'white', 
							
							
			            }
			        },
			        {
			            selector: '.DSTEM',
			            style: {
			                'border-color': 'blue',
			                'border-width': 4,
			                'background-color': 'white', 
							
							
			            }
			        },
			        {
			            selector: '.Viterbi',
			            style: {
			                'border-color': 'orange',
			                'border-width': 4,
			                'background-color': 'white', 
							
			            }
			        },
			        {
			            selector: '.Cinema',
			            style: {
			                'border-color': 'purple',
			                'border-width': 4,
			                'background-color': 'white', 
							
			            }
			        },
			        {
			            selector: '.Marshall',
			            style: {
			                'border-color': 'red',
			                'border-width': 4,
			                'background-color': 'white', 
							
			            }
			        },
			        {
			            selector: '.Annenberg',
			            style: {
			                'border-color': 'gold',
			                'border-width': 4,
			                'background-color': 'white', 
							
			            }
			        },
			        {
			            selector: '.LDornsife',
			            style: {
			                'border-color': 'pink',
			                'border-width': 4,
			                'background-color': 'white', 
							
			            }
			        },
			        {
			            selector: 'node.highlight',
			            style: {
			               
			            }
			        },
			        {
			            selector: 'node.semitransp',
			            style:{ 'opacity': '0.5' }
			        },
			        {
			            selector: 'edge.highlight',
			            style: { 'mid-target-arrow-color': '#FFF' }
			        },
			        {
			            selector: 'edge.semitransp',
			            style:{ 'opacity': '0.2' }
			        }
			        ] 
			});
		
			// Make nodes ungrabbable/unmovable
			cy.nodes().ungrabify();
			
			// Highlight neighboring nodes with edges upon hover
			cy.on('mouseover', 'node', function(e) {
			    var sel = e.target;
			    cy.elements()
			        .difference(sel.outgoers()
			            .union(sel.incomers()))
			        .not(sel)
			        .addClass('semitransp');
			    sel.addClass('highlight')
			        .outgoers()
			        .union(sel.incomers())
			        .addClass('highlight');
			});
			cy.on('mouseout', 'node', function(e) {
			    var sel = e.target;
			    cy.elements()
			        .removeClass('semitransp');
			    sel.removeClass('highlight')
			        .outgoers()
			        .union(sel.incomers())
			        .removeClass('highlight');
			});
			
			// Display tooltip upon click with QTip (jQuery)
			cy.elements('node').qtip({
					content: 
						function() {
							return '<b>' + this.data('name') + '</b>' + '<br />' + this.data('continent') + '<br />' + 'Troops: ' + this.data('value');
						},
					
					position: {
						my: 'top center',
						at: 'bottom center'
					},
					style: {
						classes: 'qtip-bootstrap',
						tip: {
							width: 16,
							height: 8
						}
					},
					//show: {
						//event: 'mouseover'
					//},
					//hide: {
						//event: 'mouseout'
					//}
			});
});
		