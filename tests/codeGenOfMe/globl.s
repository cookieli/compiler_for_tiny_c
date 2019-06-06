.file "tests/codeGenOfMe/globl.dcf"
.text
.comm	a,8, 8
.comm	b,8, 8
.comm	c,1, 1
.comm	d,1, 1
.comm	e,40,32
.comm	l,10,8
.section .rodata
.LC0:
.string "%d, %d\n"
.LC1:
.string "array e: %d, %d\n"
.LC2:
.string "bool: %d, %d\n"
.LC3:
.string "array l: %d, %d, %d, %d, %d, %d, %d, %d, %d, %d\n"
.text
.globl main
.type main, @function
main:
pushq %rbp
movq %rsp, %rbp
subq $16, %rsp
movq $3,a(%rip)
movq $5,b(%rip)
movq b(%rip),%rax
movq a(%rip),%r10
addq %r10,%rax
movq %rax,b(%rip)
movq $4,e(%rip)
movq $5,24+e(%rip)
leaq e(%rip),%rax
movq a(%rip),%r10
movq b(%rip),%r11
movq %r11,(%rax,%r10,8)
movb $1,c(%rip)
movb $0,d(%rip)
leaq l(%rip),%rax
movq b(%rip),%r10
movb $1,(%rax,%r10,1)
leaq l(%rip),%rax
movq b(%rip),%r10
movb (%rax,%r10,1),%r11b
movb %r11b,-1(%rbp)
leaq l(%rip),%rax
movq a(%rip),%r10
movb -1(%rbp),%r11b
movb %r11b,(%rax,%r10,1)
movq b(%rip),%rdx
movq a(%rip),%rsi
leaq .LC0(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movq 24+e(%rip),%rdx
movq e(%rip),%rsi
leaq .LC1(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
movsbq d(%rip),%rdx
movsbq c(%rip),%rsi
leaq .LC2(%rip),%rdi
movq $0,%rax
call printf@PLT
movq $0,%rax
subq $8,%rsp
movsbq 9+l(%rip),%rdi
pushq %rdi
movsbq 8+l(%rip),%rdi
pushq %rdi
movsbq 7+l(%rip),%rdi
pushq %rdi
movsbq 6+l(%rip),%rdi
pushq %rdi
movsbq 5+l(%rip),%rdi
pushq %rdi
movsbq 4+l(%rip),%r9
movsbq 3+l(%rip),%r8
movsbq 2+l(%rip),%rcx
movsbq 1+l(%rip),%rdx
movsbq l(%rip),%rsi
leaq .LC3(%rip),%rdi
movq $0,%rax
call printf@PLT
addq $48,%rsp
movq $0,%rax
leave
ret

